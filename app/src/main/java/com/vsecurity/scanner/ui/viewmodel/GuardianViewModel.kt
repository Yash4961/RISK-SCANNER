package com.vsecurity.scanner.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsecurity.scanner.data.model.AppSensorStats
import com.vsecurity.scanner.data.model.DailySensorSummary
import com.vsecurity.scanner.data.preferences.PreferencesManager
import com.vsecurity.scanner.data.repository.GuardianRepository
import com.vsecurity.scanner.guardian.AppNetworkUsage
import com.vsecurity.scanner.guardian.NetworkMonitorVpnService
import com.vsecurity.scanner.guardian.NetworkStatsRepository
import com.vsecurity.scanner.guardian.PrivacyGuardianService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuardianViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val guardianRepository: GuardianRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(GuardianUiState())
    val uiState: StateFlow<GuardianUiState> = _uiState.asStateFlow()

    val networkUsageFlow = NetworkStatsRepository.networkUsageFlow
    val totalUsageOverTime = NetworkStatsRepository.totalUsageOverTime
    val appUsageHistory = NetworkStatsRepository.appUsageHistory
    val isVpnRunning = NetworkStatsRepository.isVpnRunning

    init {
        loadState()
        loadActivityData()
        startPeriodicRefresh()
    }

    private fun startPeriodicRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(15_000)
                if (_uiState.value.isGuardianActive) {
                    loadActivityData()
                }
            }
        }
    }

    private fun loadState() {
        viewModelScope.launch {
            preferencesManager.guardianEnabled.collect { enabled ->
                _uiState.update { it.copy(isGuardianActive = enabled) }
            }
        }
        viewModelScope.launch {
            preferencesManager.monitorCamera.collect { camera ->
                _uiState.update { it.copy(monitorCamera = camera) }
            }
        }
        viewModelScope.launch {
            preferencesManager.monitorMicrophone.collect { mic ->
                _uiState.update { it.copy(monitorMicrophone = mic) }
            }
        }
        viewModelScope.launch {
            preferencesManager.monitorLocation.collect { location ->
                _uiState.update { it.copy(monitorLocation = location) }
            }
        }
        viewModelScope.launch {
            preferencesManager.alertOnBackground.collect { background ->
                _uiState.update { it.copy(alertOnBackground = background) }
            }
        }
        viewModelScope.launch {
            preferencesManager.networkGraphTimeframe.collect { timeframe ->
                _uiState.update { it.copy(networkGraphTimeframe = timeframe) }
            }
        }
        viewModelScope.launch {
            preferencesManager.alertOnScreenOff.collect { screenOff ->
                _uiState.update { it.copy(alertOnScreenOff = screenOff) }
            }
        }
        viewModelScope.launch {
            preferencesManager.alertOnFrequent.collect { frequent ->
                _uiState.update { it.copy(alertOnFrequent = frequent) }
            }
        }
    }

    private fun loadActivityData() {
        viewModelScope.launch {
            try {
                val appStats = guardianRepository.getAllAppStats()
                val dailySummaries = guardianRepository.getRecentDailySummaries(7)
                _uiState.update {
                    it.copy(
                        recentlyActiveApps = appStats,
                        dailySummaries = dailySummaries
                    )
                }
            } catch (_: Exception) { }
        }
    }

    fun toggleGuardian() {
        val newState = !_uiState.value.isGuardianActive
        viewModelScope.launch {
            preferencesManager.setGuardianEnabled(newState)
            if (newState) {
                val intent = Intent(context, PrivacyGuardianService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } else {
                context.stopService(Intent(context, PrivacyGuardianService::class.java))
            }
            _uiState.update { it.copy(isGuardianActive = newState) }
        }
    }
    
    fun startNetworkMonitor() {
        val intent = Intent(context, NetworkMonitorVpnService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun stopNetworkMonitor() {
        val intent = Intent(context, NetworkMonitorVpnService::class.java).apply {
            action = "STOP"
        }
        context.startService(intent)
    }

    fun setMonitorCamera(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setMonitorCamera(enabled)
            _uiState.update { it.copy(monitorCamera = enabled) }
        }
    }

    fun setMonitorMicrophone(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setMonitorMicrophone(enabled)
            _uiState.update { it.copy(monitorMicrophone = enabled) }
        }
    }

    fun setMonitorLocation(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setMonitorLocation(enabled)
            _uiState.update { it.copy(monitorLocation = enabled) }
        }
    }

    fun setAlertOnBackground(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setAlertOnBackground(enabled)
            _uiState.update { it.copy(alertOnBackground = enabled) }
        }
    }

    fun setAlertOnScreenOff(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setAlertOnScreenOff(enabled)
            _uiState.update { it.copy(alertOnScreenOff = enabled) }
        }
    }

    fun setAlertOnFrequent(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setAlertOnFrequent(enabled)
            _uiState.update { it.copy(alertOnFrequent = enabled) }
        }
    }

    fun setNetworkGraphTimeframe(timeframe: String) {
        viewModelScope.launch {
            preferencesManager.setNetworkGraphTimeframe(timeframe)
            _uiState.update { it.copy(networkGraphTimeframe = timeframe) }
        }
    }

    fun refresh() {
        loadActivityData()
    }
}

data class GuardianUiState(
    val isGuardianActive: Boolean = false,
    val monitorCamera: Boolean = true,
    val monitorMicrophone: Boolean = true,
    val monitorLocation: Boolean = true,
    val alertOnBackground: Boolean = true,
    val alertOnScreenOff: Boolean = true,
    val alertOnFrequent: Boolean = true,
    val networkGraphTimeframe: String = "60s",
    val recentlyActiveApps: List<AppSensorStats> = emptyList(),
    val dailySummaries: List<DailySensorSummary> = emptyList()
)
