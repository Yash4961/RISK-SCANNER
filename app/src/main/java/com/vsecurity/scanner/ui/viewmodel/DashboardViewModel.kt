package com.vsecurity.scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsecurity.scanner.data.model.*
import com.vsecurity.scanner.data.preferences.PreferencesManager
import com.vsecurity.scanner.data.repository.GuardianRepository
import com.vsecurity.scanner.data.repository.ScannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Dashboard ViewModel
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository,
    private val guardianRepository: GuardianRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeDashboardData()
        refresh()
    }

    private fun observeDashboardData() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Date())

        viewModelScope.launch {
            combine(
                scannerRepository.getAllAppsFlow(),
                guardianRepository.getRecentAlertsFlow(10),
                guardianRepository.getSummaryFlow(today),
                preferencesManager.guardianEnabled
            ) { apps, alerts, todaySummary, guardianActive ->
                val highRiskApps = apps.filter { it.riskLevel in listOf(RiskLevel.CRITICAL, RiskLevel.HIGH) }
                
                val overallScore = if (apps.isEmpty()) {
                    100
                } else {
                    val avgRisk = apps.map { it.riskScore }.average().toInt()
                    100 - avgRisk.coerceIn(0, 100)
                }
                
                val overallRiskLevel = when {
                    highRiskApps.size >= 5 -> RiskLevel.CRITICAL
                    highRiskApps.size >= 3 -> RiskLevel.HIGH
                    highRiskApps.isNotEmpty() -> RiskLevel.MEDIUM
                    apps.isNotEmpty() -> RiskLevel.LOW
                    else -> RiskLevel.INFO
                }

                DashboardUiState(
                    overallScore = overallScore,
                    overallRiskLevel = overallRiskLevel,
                    totalAppsScanned = apps.size,
                    highRiskApps = highRiskApps.size,
                    alertsToday = alerts.count { isToday(it.timestamp) },
                    guardianActive = guardianActive,
                    topRiskyApps = highRiskApps.take(5),
                    recentAlerts = alerts,
                    todayCameraAccesses = todaySummary?.totalCameraAccesses ?: 0,
                    todayMicAccesses = todaySummary?.totalMicrophoneAccesses ?: 0,
                    todayLocationAccesses = todaySummary?.totalLocationAccesses ?: 0,
                    todayBackgroundAccesses = todaySummary?.totalBackgroundAccesses ?: 0
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    private fun isToday(timestamp: Long): Boolean {
        val cal = Calendar.getInstance()
        val todayDay = cal.get(Calendar.DAY_OF_YEAR)
        val todayYear = cal.get(Calendar.YEAR)
        
        cal.timeInMillis = timestamp
        return cal.get(Calendar.DAY_OF_YEAR) == todayDay && cal.get(Calendar.YEAR) == todayYear
    }

    fun refresh() {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            guardianRepository.updateDailySummary(dateFormat.format(Date()))
        }
    }
}

data class DashboardUiState(
    val overallScore: Int = 100,
    val overallRiskLevel: RiskLevel = RiskLevel.INFO,
    val totalAppsScanned: Int = 0,
    val highRiskApps: Int = 0,
    val alertsToday: Int = 0,
    val guardianActive: Boolean = false,
    val topRiskyApps: List<ScannedApp> = emptyList(),
    val recentAlerts: List<PrivacyAlert> = emptyList(),
    val todayCameraAccesses: Int = 0,
    val todayMicAccesses: Int = 0,
    val todayLocationAccesses: Int = 0,
    val todayBackgroundAccesses: Int = 0
)
