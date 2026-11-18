package com.vsecurity.scanner.guardian

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.net.ConnectivityManager
import android.net.TrafficStats
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.core.app.NotificationCompat
import com.vsecurity.scanner.data.model.AlertType
import com.vsecurity.scanner.data.model.PrivacyAlert
import com.vsecurity.scanner.data.model.SensorType
import com.vsecurity.scanner.data.repository.GuardianRepository
import com.vsecurity.scanner.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * Data model for App Network Usage
 */
data class AppNetworkUsage(
    val packageName: String,
    val appName: String,
    val bytesReceived: Long,
    val bytesSent: Long,
    val isSuspicious: Boolean = false
)

/**
 * Singleton repository to hold real-time network statistics
 */
object NetworkStatsRepository {
    private val _networkUsageFlow = MutableStateFlow<List<AppNetworkUsage>>(emptyList())
    val networkUsageFlow: StateFlow<List<AppNetworkUsage>> = _networkUsageFlow.asStateFlow()

    private val _totalUsageOverTime = MutableStateFlow<List<Long>>(emptyList())
    val totalUsageOverTime: StateFlow<List<Long>> = _totalUsageOverTime.asStateFlow()

    private val _appUsageHistory = MutableStateFlow<Map<String, List<Long>>>(emptyMap())
    val appUsageHistory: StateFlow<Map<String, List<Long>>> = _appUsageHistory.asStateFlow()
    
    val isVpnRunning = MutableStateFlow(false)

    fun updateStats(stats: List<AppNetworkUsage>, totalCurrentUsage: Long, appDiffs: Map<String, Long> = emptyMap()) {
        _networkUsageFlow.value = stats
        
        // Keep up to 7 days of data points for graph
        val currentList = _totalUsageOverTime.value.toMutableList()
        currentList.add(totalCurrentUsage)
        if (currentList.size > 604800) {
            currentList.removeAt(0)
        }
        _totalUsageOverTime.value = currentList

        // App usage history
        val newHistory = _appUsageHistory.value.toMutableMap()
        for ((pkg, diff) in appDiffs) {
            val list = newHistory[pkg]?.toMutableList() ?: mutableListOf()
            list.add(diff)
            if (list.size > 3600) { // Keep 1 hour at 1 point per second to avoid OOM
                list.removeAt(0)
            }
            newHistory[pkg] = list
        }
        _appUsageHistory.value = newHistory
    }
}

/**
 * VPN Service that acts as a Network Monitor.
 */
@AndroidEntryPoint
@SuppressLint("VpnServicePolicy")
class NetworkMonitorVpnService : VpnService() {

    @Inject
    lateinit var repository: GuardianRepository

    private var vpnInterface: ParcelFileDescriptor? = null
    private val isRunning = AtomicBoolean(false)
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    
    private val NOTIFICATION_CHANNEL_ID = "vpn_monitor_channel"
    private val NOTIFICATION_ID = 999

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP") {
            stopVpn()
            return START_NOT_STICKY
        }

        if (isRunning.compareAndSet(false, true)) {
            startVpn()
        }
        return START_STICKY
    }

    private fun startVpn() {
        // Build the VPN interface
        val builder = Builder()
            .setSession("Privacy Guardian Network Monitor")
            .setMtu(1500)
            .addAddress("10.0.0.2", 32)
            
        vpnInterface = builder.establish()
        
        NetworkStatsRepository.isVpnRunning.value = true
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, createNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIFICATION_ID, createNotification())
        }

        serviceScope.launch {
            pollNetworkStats()
        }
    }

    @Suppress("DEPRECATION")
    private suspend fun pollNetworkStats() {
        val packageManager = packageManager
        val networkStatsManager = getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        val previousAppStats = mutableMapOf<Int, Pair<Long, Long>>() // uid to (rx, tx)

        // Initialize total traffic stats for the global graph
        var lastTotalRx = TrafficStats.getTotalRxBytes()
        var lastTotalTx = TrafficStats.getTotalTxBytes()

        // Cache application info to avoid heavy PM calls in the loop
        var cachedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        var lastCacheTime = 0L

        while (isRunning.get()) {
            val currentTime = System.currentTimeMillis()

            // Refresh app cache every 30 seconds
            if (currentTime - lastCacheTime > 30000) {
                cachedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                lastCacheTime = currentTime
            }

            // Calculate global traffic diff for the graph
            val currentTotalRx = TrafficStats.getTotalRxBytes()
            val currentTotalTx = TrafficStats.getTotalTxBytes()

            val globalDiffRx = if (lastTotalRx > 0) (currentTotalRx - lastTotalRx).coerceAtLeast(0) else 0
            val globalDiffTx = if (lastTotalTx > 0) (currentTotalTx - lastTotalTx).coerceAtLeast(0) else 0
            val totalGlobalDiff = globalDiffRx + globalDiffTx

            lastTotalRx = currentTotalRx
            lastTotalTx = currentTotalTx

            val currentAppStats = mutableListOf<AppNetworkUsage>()
            val appDiffs = mutableMapOf<String, Long>()
            
            val uidRx = mutableMapOf<Int, Long>()
            val uidTx = mutableMapOf<Int, Long>()

            try {
                val bucket = NetworkStats.Bucket()
                
                // Get Mobile Stats
                val mobileStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, null, 0, currentTime)
                while (mobileStats.hasNextBucket()) {
                    mobileStats.getNextBucket(bucket)
                    uidRx[bucket.uid] = (uidRx[bucket.uid] ?: 0L) + bucket.rxBytes
                    uidTx[bucket.uid] = (uidTx[bucket.uid] ?: 0L) + bucket.txBytes
                }
                mobileStats.close()

                // Get Wifi Stats
                val wifiStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, null, 0, currentTime)
                while (wifiStats.hasNextBucket()) {
                    wifiStats.getNextBucket(bucket)
                    uidRx[bucket.uid] = (uidRx[bucket.uid] ?: 0L) + bucket.rxBytes
                    uidTx[bucket.uid] = (uidTx[bucket.uid] ?: 0L) + bucket.txBytes
                }
                wifiStats.close()
            } catch (e: Exception) {
                Log.e("NetworkMonitor", "Could not query NetworkStatsManager. Permission denied?", e)
            }

            for (app in cachedApps) {
                val uid = app.uid
                val rx = uidRx[uid] ?: 0L
                val tx = uidTx[uid] ?: 0L
                
                if (rx > 0 || tx > 0) {
                    val prev = previousAppStats[uid] ?: Pair(rx, tx)
                    val diffRx = rx - prev.first
                    val diffTx = tx - prev.second

                    val appName = packageManager.getApplicationLabel(app).toString()
                    val isSuspicious = (diffTx > 1024 * 1024 * 5)

                    currentAppStats.add(
                        AppNetworkUsage(
                            packageName = app.packageName,
                            appName = appName,
                            bytesReceived = rx,
                            bytesSent = tx,
                            isSuspicious = isSuspicious
                        )
                    )

                    if (diffRx > 0 || diffTx > 0) {
                        appDiffs[app.packageName] = diffRx + diffTx
                    } else {
                        appDiffs[app.packageName] = 0L
                    }

                    if (isSuspicious && diffTx > 0) {
                        sendPrivacyAlert(app.packageName, appName)
                    }

                    previousAppStats[uid] = Pair(rx, tx)
                }
            }

            // Sort by highest traffic
            currentAppStats.sortByDescending { it.bytesReceived + it.bytesSent }

            // Update UI repository with global diff for graph and per-app stats for list
            NetworkStatsRepository.updateStats(currentAppStats, totalGlobalDiff, appDiffs)

            // Reduce polling frequency slightly to save CPU due to heavy NetworkStatsManager calls
            delay(2000)
        }
    }

    private fun sendPrivacyAlert(packageName: String, appName: String) {
        val message = "Suspicious high network usage detected from $appName"
        
        serviceScope.launch {
            val alert = PrivacyAlert(
                packageName = packageName,
                appName = appName,
                alertType = AlertType.SUSPICIOUS_PATTERN,
                sensorType = SensorType.UNKNOWN,
                message = message,
                timestamp = System.currentTimeMillis()
            )
            repository.saveAlert(alert)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("navigate_to", "alerts")
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Privacy Alert")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun stopVpn() {
        isRunning.set(false)
        NetworkStatsRepository.isVpnRunning.value = false
        vpnInterface?.close()
        vpnInterface = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopVpn()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "VPN Network Monitor",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Running Privacy Guardian VPN Monitor"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    private fun createNotification() = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        .setContentTitle("Privacy Guardian")
        .setContentText("Network Monitor is active")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .build()
}
