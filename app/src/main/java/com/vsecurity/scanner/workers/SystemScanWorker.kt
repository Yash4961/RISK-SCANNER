package com.vsecurity.scanner.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vsecurity.scanner.data.model.AlertType
import com.vsecurity.scanner.data.model.PrivacyAlert
import com.vsecurity.scanner.data.model.SensorType
import com.vsecurity.scanner.data.repository.GuardianRepository
import com.vsecurity.scanner.data.scanner.MalwareScanner
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class SystemScanWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkerEntryPoint {
        fun getMalwareScanner(): MalwareScanner
        fun getRepository(): GuardianRepository
    }

    override suspend fun doWork(): Result {
        val appContext = applicationContext
        val entryPoint = EntryPointAccessors.fromApplication(appContext, WorkerEntryPoint::class.java)
        
        val scanner = entryPoint.getMalwareScanner()
        val repository = entryPoint.getRepository()

        scanner.initialize()
        if (!scanner.isInitialized) {
            return Result.retry()
        }

        // Background scans default to checking files up to 100MB
        val maxBytes = 100L * 1024 * 1024
        val maliciousFiles = scanner.performFullSystemScan(maxBytes, { _, _ -> }, {})
        
        for (threat in maliciousFiles) {
            val alert = PrivacyAlert(
                packageName = "System File",
                appName = "Malicious File",
                alertType = AlertType.SUSPICIOUS_PATTERN,
                sensorType = SensorType.UNKNOWN,
                message = "Malicious file detected with hash: ${threat.hash}",
                timestamp = System.currentTimeMillis()
            )
            repository.saveAlert(alert)
        }

        return Result.success()
    }
}
