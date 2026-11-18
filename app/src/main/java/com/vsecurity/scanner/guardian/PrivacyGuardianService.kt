package com.vsecurity.scanner.guardian

import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.vsecurity.scanner.R
import com.vsecurity.scanner.data.model.*
import com.vsecurity.scanner.data.preferences.PreferencesManager
import com.vsecurity.scanner.data.repository.GuardianRepository
import com.vsecurity.scanner.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Background service that monitors sensor access by apps
 */
@AndroidEntryPoint
class PrivacyGuardianService : Service() {

    @Inject
    lateinit var repository: GuardianRepository

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val binder = LocalBinder()
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    private var monitoringJob: Job? = null
    private var settingsJob: Job? = null
    private var isMonitoring = false

    private val _monitoringState = MutableStateFlow<MonitoringState>(MonitoringState.Idle)
    val monitoringState: StateFlow<MonitoringState> = _monitoringState

    private lateinit var appOpsManager: AppOpsManager
    private lateinit var usageStatsManager: UsageStatsManager
    private lateinit var powerManager: PowerManager
    private lateinit var notificationManager: NotificationManager

    private var settings: GuardianSettings = GuardianSettings()
    private val lastLoggedAccessTime = mutableMapOf<String, Long>()

    companion object {
        const val CHANNEL_ID = "privacy_guardian_channel"
        const val ALERT_CHANNEL_ID = "privacy_alert_channel"
        const val NOTIFICATION_ID = 1001
        const val MONITORING_INTERVAL = 15000L // 15 seconds

        fun startService(context: Context) {
            val intent = Intent(context, PrivacyGuardianService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            context.stopService(Intent(context, PrivacyGuardianService::class.java))
        }
    }

    inner class LocalBinder : Binder() {
        fun getService(): PrivacyGuardianService = this@PrivacyGuardianService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        initializeManagers()
        createNotificationChannels()
        observeSettings()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        startMonitoring()
        return START_STICKY
    }

    override fun onDestroy() {
        stopMonitoring()
        settingsJob?.cancel()
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun observeSettings() {
        settingsJob = serviceScope.launch {
            preferencesManager.guardianSettings.collect { newSettings ->
                settings = newSettings
            }
        }
    }

    private fun initializeManagers() {
        appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Privacy Guardian",
                NotificationManager.IMPORTANCE_LOW
            )
            val alertChannel = NotificationChannel(
                ALERT_CHANNEL_ID,
                "Privacy Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(serviceChannel)
            notificationManager.createNotificationChannel(alertChannel)
        }
    }

    private fun startForeground() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Privacy Guardian Active")
            .setContentText("Monitoring sensor access...")
            .setSmallIcon(R.drawable.ic_shield)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    fun startMonitoring() {
        if (isMonitoring) return
        isMonitoring = true
        _monitoringState.value = MonitoringState.Active

        monitoringJob = serviceScope.launch {
            while (isActive && isMonitoring) {
                try {
                    checkSensorAccesses()
                } catch (_: Exception) {}
                delay(MONITORING_INTERVAL)
            }
        }
    }

    fun stopMonitoring() {
        isMonitoring = false
        monitoringJob?.cancel()
        _monitoringState.value = MonitoringState.Idle
    }

    private suspend fun checkSensorAccesses() {
        val currentTime = System.currentTimeMillis()
        val isScreenOn = powerManager.isInteractive
        val foregroundApp = getForegroundApp()
        val runningApps = getRecentlyActivePackages()

        if (settings.monitorCamera) {
            checkOpForApps(AppOpsManager.OPSTR_CAMERA, SensorType.CAMERA, currentTime, isScreenOn, foregroundApp, runningApps)
        }
        if (settings.monitorMicrophone) {
            checkOpForApps(AppOpsManager.OPSTR_RECORD_AUDIO, SensorType.MICROPHONE, currentTime, isScreenOn, foregroundApp, runningApps)
        }
        if (settings.monitorLocation) {
            checkOpForApps(AppOpsManager.OPSTR_FINE_LOCATION, SensorType.LOCATION, currentTime, isScreenOn, foregroundApp, runningApps)
        }

        updateDailySummary(currentTime)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun checkOpForApps(
        opName: String,
        sensorType: SensorType,
        currentTime: Long,
        isScreenOn: Boolean,
        foregroundApp: String?,
        runningApps: Set<String>
    ) {
        for (packageName in runningApps) {
            if (packageName == this.packageName || settings.whitelistedApps.contains(packageName)) continue

            try {
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                val mode = appOpsManager.unsafeCheckOpRawNoThrow(opName, appInfo.uid, packageName)
                
                if (mode == AppOpsManager.MODE_ALLOWED) {
                    val key = "$packageName:$sensorType"
                    val lastLogged = lastLoggedAccessTime[key] ?: 0L
                    
                    // Log if it's been more than 5 minutes, or 1 minute if it's in foreground
                    val threshold = if (packageName == foregroundApp) 60000L else 300000L
                    
                    if (currentTime - lastLogged > threshold) {
                        val isBackground = packageName != foregroundApp
                        val appName = packageManager.getApplicationLabel(appInfo).toString()
                        
                        handleSensorAccess(packageName, appName, sensorType, currentTime, isBackground, !isScreenOn)
                        lastLoggedAccessTime[key] = currentTime
                    }
                }
            } catch (_: Exception) {}
        }
    }

    private suspend fun handleSensorAccess(
        packageName: String,
        appName: String,
        sensorType: SensorType,
        accessTime: Long,
        isBackground: Boolean,
        isScreenOff: Boolean
    ) {
        val isSuspicious = (isBackground && settings.alertOnBackgroundAccess) || (isScreenOff && settings.alertOnScreenOffAccess)
        val reason = when {
            isScreenOff -> "Sensor access while screen off"
            isBackground -> "Background sensor access"
            else -> null
        }

        val logEntry = SensorAccessLog(
            packageName = packageName,
            appName = appName,
            sensorType = sensorType,
            accessTime = accessTime,
            wasInBackground = isBackground,
            wasScreenOff = isScreenOff,
            isSuspicious = isSuspicious,
            suspiciousReason = reason
        )
        repository.logSensorAccess(logEntry)
        repository.updateAppStats(packageName, appName, sensorType, isBackground)

        if (isSuspicious) {
            sendPrivacyAlert(packageName, appName, sensorType, reason!!)
        }
    }

    private suspend fun sendPrivacyAlert(
        packageName: String,
        appName: String,
        sensorType: SensorType,
        reason: String
    ) {
        val alert = PrivacyAlert(
            packageName = packageName,
            appName = appName,
            alertType = if (reason.contains("Background")) AlertType.BACKGROUND_SENSOR_ACCESS else AlertType.SCREEN_OFF_ACCESS,
            sensorType = sensorType,
            message = "$appName: $reason",
            timestamp = System.currentTimeMillis()
        )
        repository.saveAlert(alert)
        
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "alerts")
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        
        val notification = NotificationCompat.Builder(this, ALERT_CHANNEL_ID)
            .setContentTitle("Privacy Alert")
            .setContentText("$appName: $reason")
            .setSmallIcon(R.drawable.ic_alert)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), notification)
    }

    private fun getForegroundApp(): String? {
        val time = System.currentTimeMillis()
        val usageEvents = usageStatsManager.queryEvents(time - 20000, time) ?: return null
        var foregroundApp: String? = null
        val event = UsageEvents.Event()
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                foregroundApp = event.packageName
            }
        }
        return foregroundApp
    }

    private fun getRecentlyActivePackages(): Set<String> {
        val time = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 600000, time)
        return stats?.filter { it.lastTimeUsed > time - 600000 }?.map { it.packageName }?.toSet() ?: emptySet()
    }

    private suspend fun updateDailySummary(currentTime: Long) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Date(currentTime))
        repository.updateDailySummary(today)
    }
}

sealed class MonitoringState {
    object Idle : MonitoringState()
    object Active : MonitoringState()
    data class Error(val message: String) : MonitoringState()
}
