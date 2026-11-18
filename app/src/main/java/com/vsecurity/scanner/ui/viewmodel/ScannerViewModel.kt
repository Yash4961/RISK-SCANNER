package com.vsecurity.scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsecurity.scanner.data.model.RiskLevel
import com.vsecurity.scanner.data.model.ScannedApp
import com.vsecurity.scanner.data.preferences.PreferencesManager
import com.vsecurity.scanner.data.repository.ScannerRepository
import com.vsecurity.scanner.scanner.AppScanner
import com.vsecurity.scanner.scanner.ScanProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Scanner ViewModel
 */
@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val appScanner: AppScanner,
    private val scannerRepository: ScannerRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private val _scanState = MutableStateFlow<ScanState>(ScanState.Idle)
    val scanState: StateFlow<ScanState> = _scanState.asStateFlow()

    init {
        loadSavedApps()
    }

    private fun loadSavedApps() {
        viewModelScope.launch {
            val apps = scannerRepository.getAllApps()
            val lastScanTime = preferencesManager.lastScanTime.first()
            
            _uiState.update {
                it.copy(
                    scannedApps = apps,
                    lastScanTime = if (lastScanTime > 0) lastScanTime else null
                )
            }
            
            if (apps.isNotEmpty()) {
                _scanState.value = ScanState.Completed
            }
        }
    }

    fun startScan() {
        viewModelScope.launch {
            val includeSystem = preferencesManager.includeSystemApps.first()

            // Use a local list to avoid constant StateFlow updates for the full list
            val temporaryAppList = mutableListOf<ScannedApp>()

            appScanner.scanAllApps(includeSystem).collect { progress ->
                when (progress) {
                    is ScanProgress.Started -> {
                        _scanState.value = ScanState.Scanning(0, progress.totalApps, "")
                        _uiState.update { it.copy(scannedApps = emptyList()) }
                    }
                    is ScanProgress.Progress -> {
                        // 1. Update ONLY the numerical progress state (Fast)
                        _scanState.value = ScanState.Scanning(
                            current = progress.current,
                            total = progress.total,
                            currentApp = progress.app.appName
                        )

                        // 2. Buffer the app in a local list instead of the StateFlow
                        temporaryAppList.add(progress.app)

                        // 3. OPTIONAL: Only update the UI list every 10 apps to keep it smooth
                        if (progress.current % 10 == 0 || progress.current == progress.total) {
                            _uiState.update { state ->
                                state.copy(scannedApps = temporaryAppList.toList())
                            }
                        }
                    }
                    is ScanProgress.Error -> { /* Log error */ }
                    is ScanProgress.Completed -> {
                        // Final Save and Update
                        scannerRepository.saveApps(progress.report.apps)
                        preferencesManager.setLastScanTime(System.currentTimeMillis())

                        _uiState.update {
                            it.copy(
                                scannedApps = progress.report.apps,
                                lastScanTime = System.currentTimeMillis()
                            )
                        }
                        _scanState.value = ScanState.Completed
                    }
                }
            }
        }
    }

    fun setFilter(filter: RiskLevel?) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun clearScanResults() {
        viewModelScope.launch {
            scannerRepository.clearAllData()
            _uiState.update {
                it.copy(scannedApps = emptyList(), lastScanTime = null)
            }
            _scanState.value = ScanState.Idle
        }
    }
}

data class ScannerUiState(
    val scannedApps: List<ScannedApp> = emptyList(),
    val selectedFilter: RiskLevel? = null,
    val lastScanTime: Long? = null
)

sealed class ScanState {
    object Idle : ScanState()
    data class Scanning(
        val current: Int,
        val total: Int,
        val currentApp: String
    ) : ScanState()
    object Completed : ScanState()
}
