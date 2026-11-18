package com.vsecurity.scanner.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class VScannerDatabase_Impl : VScannerDatabase() {
  private val _scannedAppDao: Lazy<ScannedAppDao> = lazy {
    ScannedAppDao_Impl(this)
  }

  private val _sensorAccessLogDao: Lazy<SensorAccessLogDao> = lazy {
    SensorAccessLogDao_Impl(this)
  }

  private val _privacyAlertDao: Lazy<PrivacyAlertDao> = lazy {
    PrivacyAlertDao_Impl(this)
  }

  private val _appSensorStatsDao: Lazy<AppSensorStatsDao> = lazy {
    AppSensorStatsDao_Impl(this)
  }

  private val _dailySummaryDao: Lazy<DailySummaryDao> = lazy {
    DailySummaryDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(2, "167885d459d55c85e71ee7b207c21021", "8cf0caf05b80e8c0b6c30c88f5603314") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `scanned_apps` (`packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, `versionName` TEXT NOT NULL, `versionCode` INTEGER NOT NULL, `targetSdk` INTEGER NOT NULL, `minSdk` INTEGER NOT NULL, `permissions` TEXT NOT NULL, `dangerousPermissions` TEXT NOT NULL, `riskScore` INTEGER NOT NULL, `riskLevel` TEXT NOT NULL, `installTime` INTEGER NOT NULL, `lastUpdateTime` INTEGER NOT NULL, `isSystemApp` INTEGER NOT NULL, `isFromPlayStore` INTEGER NOT NULL, `installerSource` TEXT NOT NULL, `lastScanned` INTEGER NOT NULL, PRIMARY KEY(`packageName`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `sensor_access_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, `sensorType` TEXT NOT NULL, `accessTime` INTEGER NOT NULL, `duration` INTEGER NOT NULL, `wasInBackground` INTEGER NOT NULL, `wasScreenOff` INTEGER NOT NULL, `isSuspicious` INTEGER NOT NULL, `suspiciousReason` TEXT)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `privacy_alerts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, `alertType` TEXT NOT NULL, `sensorType` TEXT NOT NULL, `message` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `wasAcknowledged` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `app_sensor_stats` (`packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, `cameraAccessCount` INTEGER NOT NULL, `microphoneAccessCount` INTEGER NOT NULL, `locationAccessCount` INTEGER NOT NULL, `bodySensorAccessCount` INTEGER NOT NULL, `totalBackgroundAccesses` INTEGER NOT NULL, `lastCameraAccess` INTEGER, `lastMicrophoneAccess` INTEGER, `lastLocationAccess` INTEGER, `lastUpdated` INTEGER NOT NULL, PRIMARY KEY(`packageName`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `daily_sensor_summary` (`date` TEXT NOT NULL, `totalCameraAccesses` INTEGER NOT NULL, `totalMicrophoneAccesses` INTEGER NOT NULL, `totalLocationAccesses` INTEGER NOT NULL, `totalBackgroundAccesses` INTEGER NOT NULL, `uniqueAppsUsingCamera` INTEGER NOT NULL, `uniqueAppsUsingMicrophone` INTEGER NOT NULL, `uniqueAppsUsingLocation` INTEGER NOT NULL, `alertsTriggered` INTEGER NOT NULL, PRIMARY KEY(`date`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '167885d459d55c85e71ee7b207c21021')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `scanned_apps`")
        connection.execSQL("DROP TABLE IF EXISTS `sensor_access_logs`")
        connection.execSQL("DROP TABLE IF EXISTS `privacy_alerts`")
        connection.execSQL("DROP TABLE IF EXISTS `app_sensor_stats`")
        connection.execSQL("DROP TABLE IF EXISTS `daily_sensor_summary`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsScannedApps: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsScannedApps.put("packageName", TableInfo.Column("packageName", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("appName", TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("versionName", TableInfo.Column("versionName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("versionCode", TableInfo.Column("versionCode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("targetSdk", TableInfo.Column("targetSdk", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("minSdk", TableInfo.Column("minSdk", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("permissions", TableInfo.Column("permissions", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("dangerousPermissions", TableInfo.Column("dangerousPermissions", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("riskScore", TableInfo.Column("riskScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("riskLevel", TableInfo.Column("riskLevel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("installTime", TableInfo.Column("installTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("lastUpdateTime", TableInfo.Column("lastUpdateTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("isSystemApp", TableInfo.Column("isSystemApp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("isFromPlayStore", TableInfo.Column("isFromPlayStore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("installerSource", TableInfo.Column("installerSource", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsScannedApps.put("lastScanned", TableInfo.Column("lastScanned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysScannedApps: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesScannedApps: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoScannedApps: TableInfo = TableInfo("scanned_apps", _columnsScannedApps, _foreignKeysScannedApps, _indicesScannedApps)
        val _existingScannedApps: TableInfo = read(connection, "scanned_apps")
        if (!_infoScannedApps.equals(_existingScannedApps)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |scanned_apps(com.vsecurity.scanner.data.model.ScannedApp).
              | Expected:
              |""".trimMargin() + _infoScannedApps + """
              |
              | Found:
              |""".trimMargin() + _existingScannedApps)
        }
        val _columnsSensorAccessLogs: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSensorAccessLogs.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSensorAccessLogs.put("packageName", TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSensorAccessLogs.put("appName", TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSensorAccessLogs.put("sensorType", TableInfo.Column("sensorType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSensorAccessLogs.put("accessTime", TableInfo.Column("accessTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSensorAccessLogs.put("duration", TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSensorAccessLogs.put("wasInBackground", TableInfo.Column("wasInBackground", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSensorAccessLogs.put("wasScreenOff", TableInfo.Column("wasScreenOff", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSensorAccessLogs.put("isSuspicious", TableInfo.Column("isSuspicious", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSensorAccessLogs.put("suspiciousReason", TableInfo.Column("suspiciousReason", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSensorAccessLogs: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSensorAccessLogs: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSensorAccessLogs: TableInfo = TableInfo("sensor_access_logs", _columnsSensorAccessLogs, _foreignKeysSensorAccessLogs, _indicesSensorAccessLogs)
        val _existingSensorAccessLogs: TableInfo = read(connection, "sensor_access_logs")
        if (!_infoSensorAccessLogs.equals(_existingSensorAccessLogs)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |sensor_access_logs(com.vsecurity.scanner.data.model.SensorAccessLog).
              | Expected:
              |""".trimMargin() + _infoSensorAccessLogs + """
              |
              | Found:
              |""".trimMargin() + _existingSensorAccessLogs)
        }
        val _columnsPrivacyAlerts: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPrivacyAlerts.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivacyAlerts.put("packageName", TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivacyAlerts.put("appName", TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivacyAlerts.put("alertType", TableInfo.Column("alertType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivacyAlerts.put("sensorType", TableInfo.Column("sensorType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivacyAlerts.put("message", TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivacyAlerts.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivacyAlerts.put("wasAcknowledged", TableInfo.Column("wasAcknowledged", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPrivacyAlerts: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPrivacyAlerts: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPrivacyAlerts: TableInfo = TableInfo("privacy_alerts", _columnsPrivacyAlerts, _foreignKeysPrivacyAlerts, _indicesPrivacyAlerts)
        val _existingPrivacyAlerts: TableInfo = read(connection, "privacy_alerts")
        if (!_infoPrivacyAlerts.equals(_existingPrivacyAlerts)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |privacy_alerts(com.vsecurity.scanner.data.model.PrivacyAlert).
              | Expected:
              |""".trimMargin() + _infoPrivacyAlerts + """
              |
              | Found:
              |""".trimMargin() + _existingPrivacyAlerts)
        }
        val _columnsAppSensorStats: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAppSensorStats.put("packageName", TableInfo.Column("packageName", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSensorStats.put("appName", TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSensorStats.put("cameraAccessCount", TableInfo.Column("cameraAccessCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSensorStats.put("microphoneAccessCount", TableInfo.Column("microphoneAccessCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSensorStats.put("locationAccessCount", TableInfo.Column("locationAccessCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSensorStats.put("bodySensorAccessCount", TableInfo.Column("bodySensorAccessCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSensorStats.put("totalBackgroundAccesses", TableInfo.Column("totalBackgroundAccesses", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSensorStats.put("lastCameraAccess", TableInfo.Column("lastCameraAccess", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSensorStats.put("lastMicrophoneAccess", TableInfo.Column("lastMicrophoneAccess", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSensorStats.put("lastLocationAccess", TableInfo.Column("lastLocationAccess", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSensorStats.put("lastUpdated", TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAppSensorStats: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesAppSensorStats: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoAppSensorStats: TableInfo = TableInfo("app_sensor_stats", _columnsAppSensorStats, _foreignKeysAppSensorStats, _indicesAppSensorStats)
        val _existingAppSensorStats: TableInfo = read(connection, "app_sensor_stats")
        if (!_infoAppSensorStats.equals(_existingAppSensorStats)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |app_sensor_stats(com.vsecurity.scanner.data.model.AppSensorStats).
              | Expected:
              |""".trimMargin() + _infoAppSensorStats + """
              |
              | Found:
              |""".trimMargin() + _existingAppSensorStats)
        }
        val _columnsDailySensorSummary: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDailySensorSummary.put("date", TableInfo.Column("date", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDailySensorSummary.put("totalCameraAccesses", TableInfo.Column("totalCameraAccesses", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDailySensorSummary.put("totalMicrophoneAccesses", TableInfo.Column("totalMicrophoneAccesses", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDailySensorSummary.put("totalLocationAccesses", TableInfo.Column("totalLocationAccesses", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDailySensorSummary.put("totalBackgroundAccesses", TableInfo.Column("totalBackgroundAccesses", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDailySensorSummary.put("uniqueAppsUsingCamera", TableInfo.Column("uniqueAppsUsingCamera", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDailySensorSummary.put("uniqueAppsUsingMicrophone", TableInfo.Column("uniqueAppsUsingMicrophone", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDailySensorSummary.put("uniqueAppsUsingLocation", TableInfo.Column("uniqueAppsUsingLocation", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDailySensorSummary.put("alertsTriggered", TableInfo.Column("alertsTriggered", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDailySensorSummary: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDailySensorSummary: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoDailySensorSummary: TableInfo = TableInfo("daily_sensor_summary", _columnsDailySensorSummary, _foreignKeysDailySensorSummary, _indicesDailySensorSummary)
        val _existingDailySensorSummary: TableInfo = read(connection, "daily_sensor_summary")
        if (!_infoDailySensorSummary.equals(_existingDailySensorSummary)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |daily_sensor_summary(com.vsecurity.scanner.data.model.DailySensorSummary).
              | Expected:
              |""".trimMargin() + _infoDailySensorSummary + """
              |
              | Found:
              |""".trimMargin() + _existingDailySensorSummary)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "scanned_apps", "sensor_access_logs", "privacy_alerts", "app_sensor_stats", "daily_sensor_summary")
  }

  public override fun clearAllTables() {
    super.performClear(false, "scanned_apps", "sensor_access_logs", "privacy_alerts", "app_sensor_stats", "daily_sensor_summary")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ScannedAppDao::class, ScannedAppDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SensorAccessLogDao::class, SensorAccessLogDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PrivacyAlertDao::class, PrivacyAlertDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(AppSensorStatsDao::class, AppSensorStatsDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(DailySummaryDao::class, DailySummaryDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun scannedAppDao(): ScannedAppDao = _scannedAppDao.value

  public override fun sensorAccessLogDao(): SensorAccessLogDao = _sensorAccessLogDao.value

  public override fun privacyAlertDao(): PrivacyAlertDao = _privacyAlertDao.value

  public override fun appSensorStatsDao(): AppSensorStatsDao = _appSensorStatsDao.value

  public override fun dailySummaryDao(): DailySummaryDao = _dailySummaryDao.value
}
