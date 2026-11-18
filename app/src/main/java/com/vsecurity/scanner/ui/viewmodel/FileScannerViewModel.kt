package com.vsecurity.scanner.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.vsecurity.scanner.data.preferences.PreferencesManager
import com.vsecurity.scanner.data.scanner.MalwareScanner
import com.vsecurity.scanner.data.scanner.ScanResult
import com.vsecurity.scanner.workers.SystemScanWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class FileScannerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val malwareScanner: MalwareScanner,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<FileScannerUiState>(FileScannerUiState.Initializing)
    val uiState: StateFlow<FileScannerUiState> = _uiState.asStateFlow()

    private val _systemScanState = MutableStateFlow<SystemScanState>(SystemScanState.Idle)
    val systemScanState: StateFlow<SystemScanState> = _systemScanState.asStateFlow()

    val periodicScanEnabled = preferencesManager.periodicFileScanEnabled
    val periodicScanInterval = preferencesManager.periodicFileScanIntervalDays

    private var scanJob: Job? = null
    private val isPaused = MutableStateFlow(false)

    // Manual scan size setting (Default to 100MB, 0 means no limit)
    private val _maxScanSizeMb = MutableStateFlow(100L)
    val maxScanSizeMb: StateFlow<Long> = _maxScanSizeMb.asStateFlow()

    init {
        initializeScanner()
    }

    private fun initializeScanner() {
        viewModelScope.launch {
            _uiState.value = FileScannerUiState.Initializing
            malwareScanner.initialize()
            _uiState.value = FileScannerUiState.Idle
        }
    }

    fun scanFile(uri: Uri, fileName: String) {
        viewModelScope.launch {
            _uiState.value = FileScannerUiState.Scanning(fileName)
            val result = malwareScanner.scanFile(uri)
            
            _uiState.value = when (result) {
                is ScanResult.Safe -> FileScannerUiState.Safe(fileName, result.hash)
                is ScanResult.Malicious -> FileScannerUiState.Malicious(fileName, result.hash)
                is ScanResult.Error -> FileScannerUiState.Error(result.message)
            }
        }
    }
    
    fun setMaxScanSizeMb(sizeMb: Long) {
        _maxScanSizeMb.value = sizeMb
    }

    fun performFullSystemScan() {
        if (scanJob?.isActive == true) return
        
        isPaused.value = false
        scanJob = viewModelScope.launch {
            _systemScanState.value = SystemScanState.Scanning(0, "Starting scan...", false)
            
            if (!malwareScanner.isInitialized) {
                malwareScanner.initialize()
            }
            
            val maxBytes = if (_maxScanSizeMb.value == 0L) 0L else _maxScanSizeMb.value * 1024 * 1024
            
            val threats = malwareScanner.performFullSystemScan(
                maxSizeBytes = maxBytes,
                onProgress = { scanned, file ->
                    _systemScanState.value = SystemScanState.Scanning(scanned, file, isPaused.value)
                },
                checkPause = {
                    if (isPaused.value) {
                        _systemScanState.update { if (it is SystemScanState.Scanning) it.copy(isPaused = true) else it }
                        isPaused.first { !it } // Suspend until unpaused
                        _systemScanState.update { if (it is SystemScanState.Scanning) it.copy(isPaused = false) else it }
                    }
                }
            )
            
            // Only update to finished if we weren't cancelled
            if (scanJob?.isCancelled == false) {
                _systemScanState.value = SystemScanState.Finished(threats.size)
            }
        }
    }
    
    fun pauseSystemScan() {
        isPaused.value = true
    }
    
    fun resumeSystemScan() {
        isPaused.value = false
    }
    
    fun cancelSystemScan() {
        scanJob?.cancel()
        _systemScanState.value = SystemScanState.Idle
    }

    fun setPeriodicScanEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setPeriodicFileScanEnabled(enabled)
            val currentInterval = periodicScanInterval.first()
            updateWorkerSchedule(enabled, currentInterval)
        }
    }

    fun setPeriodicScanInterval(days: Long) {
        viewModelScope.launch {
            preferencesManager.setPeriodicFileScanIntervalDays(days)
            val isEnabled = periodicScanEnabled.first()
            if (isEnabled) {
                updateWorkerSchedule(true, days)
            }
        }
    }

    private fun updateWorkerSchedule(enabled: Boolean, intervalDays: Long) {
        val workManager = WorkManager.getInstance(context)
        val workName = "PeriodicSystemScan"

        if (enabled) {
            val request = PeriodicWorkRequestBuilder<SystemScanWorker>(intervalDays, TimeUnit.DAYS)
                .build()
            workManager.enqueueUniquePeriodicWork(
                workName,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        } else {
            workManager.cancelUniqueWork(workName)
        }
    }

    fun reset() {
        _uiState.value = FileScannerUiState.Idle
    }
    
    fun resetSystemScan() {
        scanJob?.cancel()
        _systemScanState.value = SystemScanState.Idle
    }
}

sealed class FileScannerUiState {
    object Initializing : FileScannerUiState()
    object Idle : FileScannerUiState()
    data class Scanning(val fileName: String) : FileScannerUiState()
    data class Safe(val fileName: String, val hash: String) : FileScannerUiState()
    data class Malicious(val fileName: String, val hash: String) : FileScannerUiState()
    data class Error(val message: String) : FileScannerUiState()
}

sealed class SystemScanState {
    object Idle : SystemScanState()
    data class Scanning(val scannedCount: Int, val currentFile: String, val isPaused: Boolean) : SystemScanState()
    data class Finished(val threatsFound: Int) : SystemScanState()
}
