package com.vsecurity.scanner.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.vsecurity.scanner.`data`.model.AppSensorStats
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppSensorStatsDao_Impl(
  __db: RoomDatabase,
) : AppSensorStatsDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAppSensorStats: EntityInsertAdapter<AppSensorStats>
  init {
    this.__db = __db
    this.__insertAdapterOfAppSensorStats = object : EntityInsertAdapter<AppSensorStats>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `app_sensor_stats` (`packageName`,`appName`,`cameraAccessCount`,`microphoneAccessCount`,`locationAccessCount`,`bodySensorAccessCount`,`totalBackgroundAccesses`,`lastCameraAccess`,`lastMicrophoneAccess`,`lastLocationAccess`,`lastUpdated`) VALUES (?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AppSensorStats) {
        statement.bindText(1, entity.packageName)
        statement.bindText(2, entity.appName)
        statement.bindLong(3, entity.cameraAccessCount.toLong())
        statement.bindLong(4, entity.microphoneAccessCount.toLong())
        statement.bindLong(5, entity.locationAccessCount.toLong())
        statement.bindLong(6, entity.bodySensorAccessCount.toLong())
        statement.bindLong(7, entity.totalBackgroundAccesses.toLong())
        val _tmpLastCameraAccess: Long? = entity.lastCameraAccess
        if (_tmpLastCameraAccess == null) {
          statement.bindNull(8)
        } else {
          statement.bindLong(8, _tmpLastCameraAccess)
        }
        val _tmpLastMicrophoneAccess: Long? = entity.lastMicrophoneAccess
        if (_tmpLastMicrophoneAccess == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpLastMicrophoneAccess)
        }
        val _tmpLastLocationAccess: Long? = entity.lastLocationAccess
        if (_tmpLastLocationAccess == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmpLastLocationAccess)
        }
        statement.bindLong(11, entity.lastUpdated)
      }
    }
  }

  public override suspend fun insertOrUpdate(stats: AppSensorStats): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfAppSensorStats.insert(_connection, stats)
  }

  public override suspend fun getAllStats(): List<AppSensorStats> {
    val _sql: String = "SELECT * FROM app_sensor_stats ORDER BY (cameraAccessCount + microphoneAccessCount + locationAccessCount) DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfCameraAccessCount: Int = getColumnIndexOrThrow(_stmt, "cameraAccessCount")
        val _columnIndexOfMicrophoneAccessCount: Int = getColumnIndexOrThrow(_stmt, "microphoneAccessCount")
        val _columnIndexOfLocationAccessCount: Int = getColumnIndexOrThrow(_stmt, "locationAccessCount")
        val _columnIndexOfBodySensorAccessCount: Int = getColumnIndexOrThrow(_stmt, "bodySensorAccessCount")
        val _columnIndexOfTotalBackgroundAccesses: Int = getColumnIndexOrThrow(_stmt, "totalBackgroundAccesses")
        val _columnIndexOfLastCameraAccess: Int = getColumnIndexOrThrow(_stmt, "lastCameraAccess")
        val _columnIndexOfLastMicrophoneAccess: Int = getColumnIndexOrThrow(_stmt, "lastMicrophoneAccess")
        val _columnIndexOfLastLocationAccess: Int = getColumnIndexOrThrow(_stmt, "lastLocationAccess")
        val _columnIndexOfLastUpdated: Int = getColumnIndexOrThrow(_stmt, "lastUpdated")
        val _result: MutableList<AppSensorStats> = mutableListOf()
        while (_stmt.step()) {
          val _item: AppSensorStats
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpCameraAccessCount: Int
          _tmpCameraAccessCount = _stmt.getLong(_columnIndexOfCameraAccessCount).toInt()
          val _tmpMicrophoneAccessCount: Int
          _tmpMicrophoneAccessCount = _stmt.getLong(_columnIndexOfMicrophoneAccessCount).toInt()
          val _tmpLocationAccessCount: Int
          _tmpLocationAccessCount = _stmt.getLong(_columnIndexOfLocationAccessCount).toInt()
          val _tmpBodySensorAccessCount: Int
          _tmpBodySensorAccessCount = _stmt.getLong(_columnIndexOfBodySensorAccessCount).toInt()
          val _tmpTotalBackgroundAccesses: Int
          _tmpTotalBackgroundAccesses = _stmt.getLong(_columnIndexOfTotalBackgroundAccesses).toInt()
          val _tmpLastCameraAccess: Long?
          if (_stmt.isNull(_columnIndexOfLastCameraAccess)) {
            _tmpLastCameraAccess = null
          } else {
            _tmpLastCameraAccess = _stmt.getLong(_columnIndexOfLastCameraAccess)
          }
          val _tmpLastMicrophoneAccess: Long?
          if (_stmt.isNull(_columnIndexOfLastMicrophoneAccess)) {
            _tmpLastMicrophoneAccess = null
          } else {
            _tmpLastMicrophoneAccess = _stmt.getLong(_columnIndexOfLastMicrophoneAccess)
          }
          val _tmpLastLocationAccess: Long?
          if (_stmt.isNull(_columnIndexOfLastLocationAccess)) {
            _tmpLastLocationAccess = null
          } else {
            _tmpLastLocationAccess = _stmt.getLong(_columnIndexOfLastLocationAccess)
          }
          val _tmpLastUpdated: Long
          _tmpLastUpdated = _stmt.getLong(_columnIndexOfLastUpdated)
          _item = AppSensorStats(_tmpPackageName,_tmpAppName,_tmpCameraAccessCount,_tmpMicrophoneAccessCount,_tmpLocationAccessCount,_tmpBodySensorAccessCount,_tmpTotalBackgroundAccesses,_tmpLastCameraAccess,_tmpLastMicrophoneAccess,_tmpLastLocationAccess,_tmpLastUpdated)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getStatsForApp(packageName: String): AppSensorStats? {
    val _sql: String = "SELECT * FROM app_sensor_stats WHERE packageName = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, packageName)
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfCameraAccessCount: Int = getColumnIndexOrThrow(_stmt, "cameraAccessCount")
        val _columnIndexOfMicrophoneAccessCount: Int = getColumnIndexOrThrow(_stmt, "microphoneAccessCount")
        val _columnIndexOfLocationAccessCount: Int = getColumnIndexOrThrow(_stmt, "locationAccessCount")
        val _columnIndexOfBodySensorAccessCount: Int = getColumnIndexOrThrow(_stmt, "bodySensorAccessCount")
        val _columnIndexOfTotalBackgroundAccesses: Int = getColumnIndexOrThrow(_stmt, "totalBackgroundAccesses")
        val _columnIndexOfLastCameraAccess: Int = getColumnIndexOrThrow(_stmt, "lastCameraAccess")
        val _columnIndexOfLastMicrophoneAccess: Int = getColumnIndexOrThrow(_stmt, "lastMicrophoneAccess")
        val _columnIndexOfLastLocationAccess: Int = getColumnIndexOrThrow(_stmt, "lastLocationAccess")
        val _columnIndexOfLastUpdated: Int = getColumnIndexOrThrow(_stmt, "lastUpdated")
        val _result: AppSensorStats?
        if (_stmt.step()) {
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpCameraAccessCount: Int
          _tmpCameraAccessCount = _stmt.getLong(_columnIndexOfCameraAccessCount).toInt()
          val _tmpMicrophoneAccessCount: Int
          _tmpMicrophoneAccessCount = _stmt.getLong(_columnIndexOfMicrophoneAccessCount).toInt()
          val _tmpLocationAccessCount: Int
          _tmpLocationAccessCount = _stmt.getLong(_columnIndexOfLocationAccessCount).toInt()
          val _tmpBodySensorAccessCount: Int
          _tmpBodySensorAccessCount = _stmt.getLong(_columnIndexOfBodySensorAccessCount).toInt()
          val _tmpTotalBackgroundAccesses: Int
          _tmpTotalBackgroundAccesses = _stmt.getLong(_columnIndexOfTotalBackgroundAccesses).toInt()
          val _tmpLastCameraAccess: Long?
          if (_stmt.isNull(_columnIndexOfLastCameraAccess)) {
            _tmpLastCameraAccess = null
          } else {
            _tmpLastCameraAccess = _stmt.getLong(_columnIndexOfLastCameraAccess)
          }
          val _tmpLastMicrophoneAccess: Long?
          if (_stmt.isNull(_columnIndexOfLastMicrophoneAccess)) {
            _tmpLastMicrophoneAccess = null
          } else {
            _tmpLastMicrophoneAccess = _stmt.getLong(_columnIndexOfLastMicrophoneAccess)
          }
          val _tmpLastLocationAccess: Long?
          if (_stmt.isNull(_columnIndexOfLastLocationAccess)) {
            _tmpLastLocationAccess = null
          } else {
            _tmpLastLocationAccess = _stmt.getLong(_columnIndexOfLastLocationAccess)
          }
          val _tmpLastUpdated: Long
          _tmpLastUpdated = _stmt.getLong(_columnIndexOfLastUpdated)
          _result = AppSensorStats(_tmpPackageName,_tmpAppName,_tmpCameraAccessCount,_tmpMicrophoneAccessCount,_tmpLocationAccessCount,_tmpBodySensorAccessCount,_tmpTotalBackgroundAccesses,_tmpLastCameraAccess,_tmpLastMicrophoneAccess,_tmpLastLocationAccess,_tmpLastUpdated)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTopBackgroundAccessors(limit: Int): List<AppSensorStats> {
    val _sql: String = "SELECT * FROM app_sensor_stats ORDER BY totalBackgroundAccesses DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfCameraAccessCount: Int = getColumnIndexOrThrow(_stmt, "cameraAccessCount")
        val _columnIndexOfMicrophoneAccessCount: Int = getColumnIndexOrThrow(_stmt, "microphoneAccessCount")
        val _columnIndexOfLocationAccessCount: Int = getColumnIndexOrThrow(_stmt, "locationAccessCount")
        val _columnIndexOfBodySensorAccessCount: Int = getColumnIndexOrThrow(_stmt, "bodySensorAccessCount")
        val _columnIndexOfTotalBackgroundAccesses: Int = getColumnIndexOrThrow(_stmt, "totalBackgroundAccesses")
        val _columnIndexOfLastCameraAccess: Int = getColumnIndexOrThrow(_stmt, "lastCameraAccess")
        val _columnIndexOfLastMicrophoneAccess: Int = getColumnIndexOrThrow(_stmt, "lastMicrophoneAccess")
        val _columnIndexOfLastLocationAccess: Int = getColumnIndexOrThrow(_stmt, "lastLocationAccess")
        val _columnIndexOfLastUpdated: Int = getColumnIndexOrThrow(_stmt, "lastUpdated")
        val _result: MutableList<AppSensorStats> = mutableListOf()
        while (_stmt.step()) {
          val _item: AppSensorStats
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpCameraAccessCount: Int
          _tmpCameraAccessCount = _stmt.getLong(_columnIndexOfCameraAccessCount).toInt()
          val _tmpMicrophoneAccessCount: Int
          _tmpMicrophoneAccessCount = _stmt.getLong(_columnIndexOfMicrophoneAccessCount).toInt()
          val _tmpLocationAccessCount: Int
          _tmpLocationAccessCount = _stmt.getLong(_columnIndexOfLocationAccessCount).toInt()
          val _tmpBodySensorAccessCount: Int
          _tmpBodySensorAccessCount = _stmt.getLong(_columnIndexOfBodySensorAccessCount).toInt()
          val _tmpTotalBackgroundAccesses: Int
          _tmpTotalBackgroundAccesses = _stmt.getLong(_columnIndexOfTotalBackgroundAccesses).toInt()
          val _tmpLastCameraAccess: Long?
          if (_stmt.isNull(_columnIndexOfLastCameraAccess)) {
            _tmpLastCameraAccess = null
          } else {
            _tmpLastCameraAccess = _stmt.getLong(_columnIndexOfLastCameraAccess)
          }
          val _tmpLastMicrophoneAccess: Long?
          if (_stmt.isNull(_columnIndexOfLastMicrophoneAccess)) {
            _tmpLastMicrophoneAccess = null
          } else {
            _tmpLastMicrophoneAccess = _stmt.getLong(_columnIndexOfLastMicrophoneAccess)
          }
          val _tmpLastLocationAccess: Long?
          if (_stmt.isNull(_columnIndexOfLastLocationAccess)) {
            _tmpLastLocationAccess = null
          } else {
            _tmpLastLocationAccess = _stmt.getLong(_columnIndexOfLastLocationAccess)
          }
          val _tmpLastUpdated: Long
          _tmpLastUpdated = _stmt.getLong(_columnIndexOfLastUpdated)
          _item = AppSensorStats(_tmpPackageName,_tmpAppName,_tmpCameraAccessCount,_tmpMicrophoneAccessCount,_tmpLocationAccessCount,_tmpBodySensorAccessCount,_tmpTotalBackgroundAccesses,_tmpLastCameraAccess,_tmpLastMicrophoneAccess,_tmpLastLocationAccess,_tmpLastUpdated)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM app_sensor_stats"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllStats() {
    val _sql: String = "DELETE FROM app_sensor_stats"
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
