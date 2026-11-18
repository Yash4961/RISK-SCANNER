package com.vsecurity.scanner.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.vsecurity.scanner.`data`.model.DangerousPermission
import com.vsecurity.scanner.`data`.model.RiskLevel
import com.vsecurity.scanner.`data`.model.ScannedApp
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ScannedAppDao_Impl(
  __db: RoomDatabase,
) : ScannedAppDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfScannedApp: EntityInsertAdapter<ScannedApp>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfScannedApp: EntityDeleteOrUpdateAdapter<ScannedApp>
  init {
    this.__db = __db
    this.__insertAdapterOfScannedApp = object : EntityInsertAdapter<ScannedApp>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `scanned_apps` (`packageName`,`appName`,`versionName`,`versionCode`,`targetSdk`,`minSdk`,`permissions`,`dangerousPermissions`,`riskScore`,`riskLevel`,`installTime`,`lastUpdateTime`,`isSystemApp`,`isFromPlayStore`,`installerSource`,`lastScanned`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ScannedApp) {
        statement.bindText(1, entity.packageName)
        statement.bindText(2, entity.appName)
        statement.bindText(3, entity.versionName)
        statement.bindLong(4, entity.versionCode)
        statement.bindLong(5, entity.targetSdk.toLong())
        statement.bindLong(6, entity.minSdk.toLong())
        val _tmp: String = __converters.fromStringList(entity.permissions)
        statement.bindText(7, _tmp)
        val _tmp_1: String = __converters.fromDangerousPermissionList(entity.dangerousPermissions)
        statement.bindText(8, _tmp_1)
        statement.bindLong(9, entity.riskScore.toLong())
        val _tmp_2: String = __converters.fromRiskLevel(entity.riskLevel)
        statement.bindText(10, _tmp_2)
        statement.bindLong(11, entity.installTime)
        statement.bindLong(12, entity.lastUpdateTime)
        val _tmp_3: Int = if (entity.isSystemApp) 1 else 0
        statement.bindLong(13, _tmp_3.toLong())
        val _tmp_4: Int = if (entity.isFromPlayStore) 1 else 0
        statement.bindLong(14, _tmp_4.toLong())
        statement.bindText(15, entity.installerSource)
        statement.bindLong(16, entity.lastScanned)
      }
    }
    this.__deleteAdapterOfScannedApp = object : EntityDeleteOrUpdateAdapter<ScannedApp>() {
      protected override fun createQuery(): String = "DELETE FROM `scanned_apps` WHERE `packageName` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ScannedApp) {
        statement.bindText(1, entity.packageName)
      }
    }
  }

  public override suspend fun insertApp(app: ScannedApp): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfScannedApp.insert(_connection, app)
  }

  public override suspend fun insertApps(apps: List<ScannedApp>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfScannedApp.insert(_connection, apps)
  }

  public override suspend fun deleteApp(app: ScannedApp): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfScannedApp.handle(_connection, app)
  }

  public override suspend fun getAllApps(): List<ScannedApp> {
    val _sql: String = "SELECT * FROM scanned_apps ORDER BY riskScore DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfVersionName: Int = getColumnIndexOrThrow(_stmt, "versionName")
        val _columnIndexOfVersionCode: Int = getColumnIndexOrThrow(_stmt, "versionCode")
        val _columnIndexOfTargetSdk: Int = getColumnIndexOrThrow(_stmt, "targetSdk")
        val _columnIndexOfMinSdk: Int = getColumnIndexOrThrow(_stmt, "minSdk")
        val _columnIndexOfPermissions: Int = getColumnIndexOrThrow(_stmt, "permissions")
        val _columnIndexOfDangerousPermissions: Int = getColumnIndexOrThrow(_stmt, "dangerousPermissions")
        val _columnIndexOfRiskScore: Int = getColumnIndexOrThrow(_stmt, "riskScore")
        val _columnIndexOfRiskLevel: Int = getColumnIndexOrThrow(_stmt, "riskLevel")
        val _columnIndexOfInstallTime: Int = getColumnIndexOrThrow(_stmt, "installTime")
        val _columnIndexOfLastUpdateTime: Int = getColumnIndexOrThrow(_stmt, "lastUpdateTime")
        val _columnIndexOfIsSystemApp: Int = getColumnIndexOrThrow(_stmt, "isSystemApp")
        val _columnIndexOfIsFromPlayStore: Int = getColumnIndexOrThrow(_stmt, "isFromPlayStore")
        val _columnIndexOfInstallerSource: Int = getColumnIndexOrThrow(_stmt, "installerSource")
        val _columnIndexOfLastScanned: Int = getColumnIndexOrThrow(_stmt, "lastScanned")
        val _result: MutableList<ScannedApp> = mutableListOf()
        while (_stmt.step()) {
          val _item: ScannedApp
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpVersionName: String
          _tmpVersionName = _stmt.getText(_columnIndexOfVersionName)
          val _tmpVersionCode: Long
          _tmpVersionCode = _stmt.getLong(_columnIndexOfVersionCode)
          val _tmpTargetSdk: Int
          _tmpTargetSdk = _stmt.getLong(_columnIndexOfTargetSdk).toInt()
          val _tmpMinSdk: Int
          _tmpMinSdk = _stmt.getLong(_columnIndexOfMinSdk).toInt()
          val _tmpPermissions: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfPermissions)
          _tmpPermissions = __converters.toStringList(_tmp)
          val _tmpDangerousPermissions: List<DangerousPermission>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfDangerousPermissions)
          _tmpDangerousPermissions = __converters.toDangerousPermissionList(_tmp_1)
          val _tmpRiskScore: Int
          _tmpRiskScore = _stmt.getLong(_columnIndexOfRiskScore).toInt()
          val _tmpRiskLevel: RiskLevel
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfRiskLevel)
          _tmpRiskLevel = __converters.toRiskLevel(_tmp_2)
          val _tmpInstallTime: Long
          _tmpInstallTime = _stmt.getLong(_columnIndexOfInstallTime)
          val _tmpLastUpdateTime: Long
          _tmpLastUpdateTime = _stmt.getLong(_columnIndexOfLastUpdateTime)
          val _tmpIsSystemApp: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsSystemApp).toInt()
          _tmpIsSystemApp = _tmp_3 != 0
          val _tmpIsFromPlayStore: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsFromPlayStore).toInt()
          _tmpIsFromPlayStore = _tmp_4 != 0
          val _tmpInstallerSource: String
          _tmpInstallerSource = _stmt.getText(_columnIndexOfInstallerSource)
          val _tmpLastScanned: Long
          _tmpLastScanned = _stmt.getLong(_columnIndexOfLastScanned)
          _item = ScannedApp(_tmpPackageName,_tmpAppName,_tmpVersionName,_tmpVersionCode,_tmpTargetSdk,_tmpMinSdk,_tmpPermissions,_tmpDangerousPermissions,_tmpRiskScore,_tmpRiskLevel,_tmpInstallTime,_tmpLastUpdateTime,_tmpIsSystemApp,_tmpIsFromPlayStore,_tmpInstallerSource,_tmpLastScanned)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllAppsFlow(): Flow<List<ScannedApp>> {
    val _sql: String = "SELECT * FROM scanned_apps ORDER BY riskScore DESC"
    return createFlow(__db, false, arrayOf("scanned_apps")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfVersionName: Int = getColumnIndexOrThrow(_stmt, "versionName")
        val _columnIndexOfVersionCode: Int = getColumnIndexOrThrow(_stmt, "versionCode")
        val _columnIndexOfTargetSdk: Int = getColumnIndexOrThrow(_stmt, "targetSdk")
        val _columnIndexOfMinSdk: Int = getColumnIndexOrThrow(_stmt, "minSdk")
        val _columnIndexOfPermissions: Int = getColumnIndexOrThrow(_stmt, "permissions")
        val _columnIndexOfDangerousPermissions: Int = getColumnIndexOrThrow(_stmt, "dangerousPermissions")
        val _columnIndexOfRiskScore: Int = getColumnIndexOrThrow(_stmt, "riskScore")
        val _columnIndexOfRiskLevel: Int = getColumnIndexOrThrow(_stmt, "riskLevel")
        val _columnIndexOfInstallTime: Int = getColumnIndexOrThrow(_stmt, "installTime")
        val _columnIndexOfLastUpdateTime: Int = getColumnIndexOrThrow(_stmt, "lastUpdateTime")
        val _columnIndexOfIsSystemApp: Int = getColumnIndexOrThrow(_stmt, "isSystemApp")
        val _columnIndexOfIsFromPlayStore: Int = getColumnIndexOrThrow(_stmt, "isFromPlayStore")
        val _columnIndexOfInstallerSource: Int = getColumnIndexOrThrow(_stmt, "installerSource")
        val _columnIndexOfLastScanned: Int = getColumnIndexOrThrow(_stmt, "lastScanned")
        val _result: MutableList<ScannedApp> = mutableListOf()
        while (_stmt.step()) {
          val _item: ScannedApp
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpVersionName: String
          _tmpVersionName = _stmt.getText(_columnIndexOfVersionName)
          val _tmpVersionCode: Long
          _tmpVersionCode = _stmt.getLong(_columnIndexOfVersionCode)
          val _tmpTargetSdk: Int
          _tmpTargetSdk = _stmt.getLong(_columnIndexOfTargetSdk).toInt()
          val _tmpMinSdk: Int
          _tmpMinSdk = _stmt.getLong(_columnIndexOfMinSdk).toInt()
          val _tmpPermissions: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfPermissions)
          _tmpPermissions = __converters.toStringList(_tmp)
          val _tmpDangerousPermissions: List<DangerousPermission>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfDangerousPermissions)
          _tmpDangerousPermissions = __converters.toDangerousPermissionList(_tmp_1)
          val _tmpRiskScore: Int
          _tmpRiskScore = _stmt.getLong(_columnIndexOfRiskScore).toInt()
          val _tmpRiskLevel: RiskLevel
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfRiskLevel)
          _tmpRiskLevel = __converters.toRiskLevel(_tmp_2)
          val _tmpInstallTime: Long
          _tmpInstallTime = _stmt.getLong(_columnIndexOfInstallTime)
          val _tmpLastUpdateTime: Long
          _tmpLastUpdateTime = _stmt.getLong(_columnIndexOfLastUpdateTime)
          val _tmpIsSystemApp: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsSystemApp).toInt()
          _tmpIsSystemApp = _tmp_3 != 0
          val _tmpIsFromPlayStore: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsFromPlayStore).toInt()
          _tmpIsFromPlayStore = _tmp_4 != 0
          val _tmpInstallerSource: String
          _tmpInstallerSource = _stmt.getText(_columnIndexOfInstallerSource)
          val _tmpLastScanned: Long
          _tmpLastScanned = _stmt.getLong(_columnIndexOfLastScanned)
          _item = ScannedApp(_tmpPackageName,_tmpAppName,_tmpVersionName,_tmpVersionCode,_tmpTargetSdk,_tmpMinSdk,_tmpPermissions,_tmpDangerousPermissions,_tmpRiskScore,_tmpRiskLevel,_tmpInstallTime,_tmpLastUpdateTime,_tmpIsSystemApp,_tmpIsFromPlayStore,_tmpInstallerSource,_tmpLastScanned)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getApp(packageName: String): ScannedApp? {
    val _sql: String = "SELECT * FROM scanned_apps WHERE packageName = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, packageName)
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfVersionName: Int = getColumnIndexOrThrow(_stmt, "versionName")
        val _columnIndexOfVersionCode: Int = getColumnIndexOrThrow(_stmt, "versionCode")
        val _columnIndexOfTargetSdk: Int = getColumnIndexOrThrow(_stmt, "targetSdk")
        val _columnIndexOfMinSdk: Int = getColumnIndexOrThrow(_stmt, "minSdk")
        val _columnIndexOfPermissions: Int = getColumnIndexOrThrow(_stmt, "permissions")
        val _columnIndexOfDangerousPermissions: Int = getColumnIndexOrThrow(_stmt, "dangerousPermissions")
        val _columnIndexOfRiskScore: Int = getColumnIndexOrThrow(_stmt, "riskScore")
        val _columnIndexOfRiskLevel: Int = getColumnIndexOrThrow(_stmt, "riskLevel")
        val _columnIndexOfInstallTime: Int = getColumnIndexOrThrow(_stmt, "installTime")
        val _columnIndexOfLastUpdateTime: Int = getColumnIndexOrThrow(_stmt, "lastUpdateTime")
        val _columnIndexOfIsSystemApp: Int = getColumnIndexOrThrow(_stmt, "isSystemApp")
        val _columnIndexOfIsFromPlayStore: Int = getColumnIndexOrThrow(_stmt, "isFromPlayStore")
        val _columnIndexOfInstallerSource: Int = getColumnIndexOrThrow(_stmt, "installerSource")
        val _columnIndexOfLastScanned: Int = getColumnIndexOrThrow(_stmt, "lastScanned")
        val _result: ScannedApp?
        if (_stmt.step()) {
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpVersionName: String
          _tmpVersionName = _stmt.getText(_columnIndexOfVersionName)
          val _tmpVersionCode: Long
          _tmpVersionCode = _stmt.getLong(_columnIndexOfVersionCode)
          val _tmpTargetSdk: Int
          _tmpTargetSdk = _stmt.getLong(_columnIndexOfTargetSdk).toInt()
          val _tmpMinSdk: Int
          _tmpMinSdk = _stmt.getLong(_columnIndexOfMinSdk).toInt()
          val _tmpPermissions: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfPermissions)
          _tmpPermissions = __converters.toStringList(_tmp)
          val _tmpDangerousPermissions: List<DangerousPermission>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfDangerousPermissions)
          _tmpDangerousPermissions = __converters.toDangerousPermissionList(_tmp_1)
          val _tmpRiskScore: Int
          _tmpRiskScore = _stmt.getLong(_columnIndexOfRiskScore).toInt()
          val _tmpRiskLevel: RiskLevel
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfRiskLevel)
          _tmpRiskLevel = __converters.toRiskLevel(_tmp_2)
          val _tmpInstallTime: Long
          _tmpInstallTime = _stmt.getLong(_columnIndexOfInstallTime)
          val _tmpLastUpdateTime: Long
          _tmpLastUpdateTime = _stmt.getLong(_columnIndexOfLastUpdateTime)
          val _tmpIsSystemApp: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsSystemApp).toInt()
          _tmpIsSystemApp = _tmp_3 != 0
          val _tmpIsFromPlayStore: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsFromPlayStore).toInt()
          _tmpIsFromPlayStore = _tmp_4 != 0
          val _tmpInstallerSource: String
          _tmpInstallerSource = _stmt.getText(_columnIndexOfInstallerSource)
          val _tmpLastScanned: Long
          _tmpLastScanned = _stmt.getLong(_columnIndexOfLastScanned)
          _result = ScannedApp(_tmpPackageName,_tmpAppName,_tmpVersionName,_tmpVersionCode,_tmpTargetSdk,_tmpMinSdk,_tmpPermissions,_tmpDangerousPermissions,_tmpRiskScore,_tmpRiskLevel,_tmpInstallTime,_tmpLastUpdateTime,_tmpIsSystemApp,_tmpIsFromPlayStore,_tmpInstallerSource,_tmpLastScanned)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getHighRiskApps(): List<ScannedApp> {
    val _sql: String = "SELECT * FROM scanned_apps WHERE riskLevel IN ('CRITICAL', 'HIGH') ORDER BY riskScore DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfVersionName: Int = getColumnIndexOrThrow(_stmt, "versionName")
        val _columnIndexOfVersionCode: Int = getColumnIndexOrThrow(_stmt, "versionCode")
        val _columnIndexOfTargetSdk: Int = getColumnIndexOrThrow(_stmt, "targetSdk")
        val _columnIndexOfMinSdk: Int = getColumnIndexOrThrow(_stmt, "minSdk")
        val _columnIndexOfPermissions: Int = getColumnIndexOrThrow(_stmt, "permissions")
        val _columnIndexOfDangerousPermissions: Int = getColumnIndexOrThrow(_stmt, "dangerousPermissions")
        val _columnIndexOfRiskScore: Int = getColumnIndexOrThrow(_stmt, "riskScore")
        val _columnIndexOfRiskLevel: Int = getColumnIndexOrThrow(_stmt, "riskLevel")
        val _columnIndexOfInstallTime: Int = getColumnIndexOrThrow(_stmt, "installTime")
        val _columnIndexOfLastUpdateTime: Int = getColumnIndexOrThrow(_stmt, "lastUpdateTime")
        val _columnIndexOfIsSystemApp: Int = getColumnIndexOrThrow(_stmt, "isSystemApp")
        val _columnIndexOfIsFromPlayStore: Int = getColumnIndexOrThrow(_stmt, "isFromPlayStore")
        val _columnIndexOfInstallerSource: Int = getColumnIndexOrThrow(_stmt, "installerSource")
        val _columnIndexOfLastScanned: Int = getColumnIndexOrThrow(_stmt, "lastScanned")
        val _result: MutableList<ScannedApp> = mutableListOf()
        while (_stmt.step()) {
          val _item: ScannedApp
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpVersionName: String
          _tmpVersionName = _stmt.getText(_columnIndexOfVersionName)
          val _tmpVersionCode: Long
          _tmpVersionCode = _stmt.getLong(_columnIndexOfVersionCode)
          val _tmpTargetSdk: Int
          _tmpTargetSdk = _stmt.getLong(_columnIndexOfTargetSdk).toInt()
          val _tmpMinSdk: Int
          _tmpMinSdk = _stmt.getLong(_columnIndexOfMinSdk).toInt()
          val _tmpPermissions: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfPermissions)
          _tmpPermissions = __converters.toStringList(_tmp)
          val _tmpDangerousPermissions: List<DangerousPermission>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfDangerousPermissions)
          _tmpDangerousPermissions = __converters.toDangerousPermissionList(_tmp_1)
          val _tmpRiskScore: Int
          _tmpRiskScore = _stmt.getLong(_columnIndexOfRiskScore).toInt()
          val _tmpRiskLevel: RiskLevel
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfRiskLevel)
          _tmpRiskLevel = __converters.toRiskLevel(_tmp_2)
          val _tmpInstallTime: Long
          _tmpInstallTime = _stmt.getLong(_columnIndexOfInstallTime)
          val _tmpLastUpdateTime: Long
          _tmpLastUpdateTime = _stmt.getLong(_columnIndexOfLastUpdateTime)
          val _tmpIsSystemApp: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsSystemApp).toInt()
          _tmpIsSystemApp = _tmp_3 != 0
          val _tmpIsFromPlayStore: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsFromPlayStore).toInt()
          _tmpIsFromPlayStore = _tmp_4 != 0
          val _tmpInstallerSource: String
          _tmpInstallerSource = _stmt.getText(_columnIndexOfInstallerSource)
          val _tmpLastScanned: Long
          _tmpLastScanned = _stmt.getLong(_columnIndexOfLastScanned)
          _item = ScannedApp(_tmpPackageName,_tmpAppName,_tmpVersionName,_tmpVersionCode,_tmpTargetSdk,_tmpMinSdk,_tmpPermissions,_tmpDangerousPermissions,_tmpRiskScore,_tmpRiskLevel,_tmpInstallTime,_tmpLastUpdateTime,_tmpIsSystemApp,_tmpIsFromPlayStore,_tmpInstallerSource,_tmpLastScanned)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAppCount(): Int {
    val _sql: String = "SELECT COUNT(*) FROM scanned_apps"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAppsByRiskLevel(level: RiskLevel): List<ScannedApp> {
    val _sql: String = "SELECT * FROM scanned_apps WHERE riskLevel = ? ORDER BY riskScore DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String = __converters.fromRiskLevel(level)
        _stmt.bindText(_argIndex, _tmp)
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfVersionName: Int = getColumnIndexOrThrow(_stmt, "versionName")
        val _columnIndexOfVersionCode: Int = getColumnIndexOrThrow(_stmt, "versionCode")
        val _columnIndexOfTargetSdk: Int = getColumnIndexOrThrow(_stmt, "targetSdk")
        val _columnIndexOfMinSdk: Int = getColumnIndexOrThrow(_stmt, "minSdk")
        val _columnIndexOfPermissions: Int = getColumnIndexOrThrow(_stmt, "permissions")
        val _columnIndexOfDangerousPermissions: Int = getColumnIndexOrThrow(_stmt, "dangerousPermissions")
        val _columnIndexOfRiskScore: Int = getColumnIndexOrThrow(_stmt, "riskScore")
        val _columnIndexOfRiskLevel: Int = getColumnIndexOrThrow(_stmt, "riskLevel")
        val _columnIndexOfInstallTime: Int = getColumnIndexOrThrow(_stmt, "installTime")
        val _columnIndexOfLastUpdateTime: Int = getColumnIndexOrThrow(_stmt, "lastUpdateTime")
        val _columnIndexOfIsSystemApp: Int = getColumnIndexOrThrow(_stmt, "isSystemApp")
        val _columnIndexOfIsFromPlayStore: Int = getColumnIndexOrThrow(_stmt, "isFromPlayStore")
        val _columnIndexOfInstallerSource: Int = getColumnIndexOrThrow(_stmt, "installerSource")
        val _columnIndexOfLastScanned: Int = getColumnIndexOrThrow(_stmt, "lastScanned")
        val _result: MutableList<ScannedApp> = mutableListOf()
        while (_stmt.step()) {
          val _item: ScannedApp
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpVersionName: String
          _tmpVersionName = _stmt.getText(_columnIndexOfVersionName)
          val _tmpVersionCode: Long
          _tmpVersionCode = _stmt.getLong(_columnIndexOfVersionCode)
          val _tmpTargetSdk: Int
          _tmpTargetSdk = _stmt.getLong(_columnIndexOfTargetSdk).toInt()
          val _tmpMinSdk: Int
          _tmpMinSdk = _stmt.getLong(_columnIndexOfMinSdk).toInt()
          val _tmpPermissions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfPermissions)
          _tmpPermissions = __converters.toStringList(_tmp_1)
          val _tmpDangerousPermissions: List<DangerousPermission>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfDangerousPermissions)
          _tmpDangerousPermissions = __converters.toDangerousPermissionList(_tmp_2)
          val _tmpRiskScore: Int
          _tmpRiskScore = _stmt.getLong(_columnIndexOfRiskScore).toInt()
          val _tmpRiskLevel: RiskLevel
          val _tmp_3: String
          _tmp_3 = _stmt.getText(_columnIndexOfRiskLevel)
          _tmpRiskLevel = __converters.toRiskLevel(_tmp_3)
          val _tmpInstallTime: Long
          _tmpInstallTime = _stmt.getLong(_columnIndexOfInstallTime)
          val _tmpLastUpdateTime: Long
          _tmpLastUpdateTime = _stmt.getLong(_columnIndexOfLastUpdateTime)
          val _tmpIsSystemApp: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsSystemApp).toInt()
          _tmpIsSystemApp = _tmp_4 != 0
          val _tmpIsFromPlayStore: Boolean
          val _tmp_5: Int
          _tmp_5 = _stmt.getLong(_columnIndexOfIsFromPlayStore).toInt()
          _tmpIsFromPlayStore = _tmp_5 != 0
          val _tmpInstallerSource: String
          _tmpInstallerSource = _stmt.getText(_columnIndexOfInstallerSource)
          val _tmpLastScanned: Long
          _tmpLastScanned = _stmt.getLong(_columnIndexOfLastScanned)
          _item = ScannedApp(_tmpPackageName,_tmpAppName,_tmpVersionName,_tmpVersionCode,_tmpTargetSdk,_tmpMinSdk,_tmpPermissions,_tmpDangerousPermissions,_tmpRiskScore,_tmpRiskLevel,_tmpInstallTime,_tmpLastUpdateTime,_tmpIsSystemApp,_tmpIsFromPlayStore,_tmpInstallerSource,_tmpLastScanned)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllApps() {
    val _sql: String = "DELETE FROM scanned_apps"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
