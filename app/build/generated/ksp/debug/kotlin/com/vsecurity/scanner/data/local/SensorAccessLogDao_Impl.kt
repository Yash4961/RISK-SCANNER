package com.vsecurity.scanner.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.vsecurity.scanner.`data`.model.SensorAccessLog
import com.vsecurity.scanner.`data`.model.SensorType
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
public class SensorAccessLogDao_Impl(
  __db: RoomDatabase,
) : SensorAccessLogDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSensorAccessLog: EntityInsertAdapter<SensorAccessLog>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfSensorAccessLog = object : EntityInsertAdapter<SensorAccessLog>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `sensor_access_logs` (`id`,`packageName`,`appName`,`sensorType`,`accessTime`,`duration`,`wasInBackground`,`wasScreenOff`,`isSuspicious`,`suspiciousReason`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SensorAccessLog) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.packageName)
        statement.bindText(3, entity.appName)
        val _tmp: String = __converters.fromSensorType(entity.sensorType)
        statement.bindText(4, _tmp)
        statement.bindLong(5, entity.accessTime)
        statement.bindLong(6, entity.duration)
        val _tmp_1: Int = if (entity.wasInBackground) 1 else 0
        statement.bindLong(7, _tmp_1.toLong())
        val _tmp_2: Int = if (entity.wasScreenOff) 1 else 0
        statement.bindLong(8, _tmp_2.toLong())
        val _tmp_3: Int = if (entity.isSuspicious) 1 else 0
        statement.bindLong(9, _tmp_3.toLong())
        val _tmpSuspiciousReason: String? = entity.suspiciousReason
        if (_tmpSuspiciousReason == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpSuspiciousReason)
        }
      }
    }
  }

  public override suspend fun insertLog(log: SensorAccessLog): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSensorAccessLog.insert(_connection, log)
  }

  public override suspend fun getRecentLogs(limit: Int): List<SensorAccessLog> {
    val _sql: String = "SELECT * FROM sensor_access_logs ORDER BY accessTime DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfSensorType: Int = getColumnIndexOrThrow(_stmt, "sensorType")
        val _columnIndexOfAccessTime: Int = getColumnIndexOrThrow(_stmt, "accessTime")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _columnIndexOfWasInBackground: Int = getColumnIndexOrThrow(_stmt, "wasInBackground")
        val _columnIndexOfWasScreenOff: Int = getColumnIndexOrThrow(_stmt, "wasScreenOff")
        val _columnIndexOfIsSuspicious: Int = getColumnIndexOrThrow(_stmt, "isSuspicious")
        val _columnIndexOfSuspiciousReason: Int = getColumnIndexOrThrow(_stmt, "suspiciousReason")
        val _result: MutableList<SensorAccessLog> = mutableListOf()
        while (_stmt.step()) {
          val _item: SensorAccessLog
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpSensorType: SensorType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfSensorType)
          _tmpSensorType = __converters.toSensorType(_tmp)
          val _tmpAccessTime: Long
          _tmpAccessTime = _stmt.getLong(_columnIndexOfAccessTime)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          val _tmpWasInBackground: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfWasInBackground).toInt()
          _tmpWasInBackground = _tmp_1 != 0
          val _tmpWasScreenOff: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfWasScreenOff).toInt()
          _tmpWasScreenOff = _tmp_2 != 0
          val _tmpIsSuspicious: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsSuspicious).toInt()
          _tmpIsSuspicious = _tmp_3 != 0
          val _tmpSuspiciousReason: String?
          if (_stmt.isNull(_columnIndexOfSuspiciousReason)) {
            _tmpSuspiciousReason = null
          } else {
            _tmpSuspiciousReason = _stmt.getText(_columnIndexOfSuspiciousReason)
          }
          _item = SensorAccessLog(_tmpId,_tmpPackageName,_tmpAppName,_tmpSensorType,_tmpAccessTime,_tmpDuration,_tmpWasInBackground,_tmpWasScreenOff,_tmpIsSuspicious,_tmpSuspiciousReason)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getLogsForApp(packageName: String, limit: Int): List<SensorAccessLog> {
    val _sql: String = "SELECT * FROM sensor_access_logs WHERE packageName = ? ORDER BY accessTime DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, packageName)
        _argIndex = 2
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfSensorType: Int = getColumnIndexOrThrow(_stmt, "sensorType")
        val _columnIndexOfAccessTime: Int = getColumnIndexOrThrow(_stmt, "accessTime")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _columnIndexOfWasInBackground: Int = getColumnIndexOrThrow(_stmt, "wasInBackground")
        val _columnIndexOfWasScreenOff: Int = getColumnIndexOrThrow(_stmt, "wasScreenOff")
        val _columnIndexOfIsSuspicious: Int = getColumnIndexOrThrow(_stmt, "isSuspicious")
        val _columnIndexOfSuspiciousReason: Int = getColumnIndexOrThrow(_stmt, "suspiciousReason")
        val _result: MutableList<SensorAccessLog> = mutableListOf()
        while (_stmt.step()) {
          val _item: SensorAccessLog
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpSensorType: SensorType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfSensorType)
          _tmpSensorType = __converters.toSensorType(_tmp)
          val _tmpAccessTime: Long
          _tmpAccessTime = _stmt.getLong(_columnIndexOfAccessTime)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          val _tmpWasInBackground: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfWasInBackground).toInt()
          _tmpWasInBackground = _tmp_1 != 0
          val _tmpWasScreenOff: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfWasScreenOff).toInt()
          _tmpWasScreenOff = _tmp_2 != 0
          val _tmpIsSuspicious: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsSuspicious).toInt()
          _tmpIsSuspicious = _tmp_3 != 0
          val _tmpSuspiciousReason: String?
          if (_stmt.isNull(_columnIndexOfSuspiciousReason)) {
            _tmpSuspiciousReason = null
          } else {
            _tmpSuspiciousReason = _stmt.getText(_columnIndexOfSuspiciousReason)
          }
          _item = SensorAccessLog(_tmpId,_tmpPackageName,_tmpAppName,_tmpSensorType,_tmpAccessTime,_tmpDuration,_tmpWasInBackground,_tmpWasScreenOff,_tmpIsSuspicious,_tmpSuspiciousReason)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getLogsBySensorType(sensorType: SensorType, limit: Int): List<SensorAccessLog> {
    val _sql: String = "SELECT * FROM sensor_access_logs WHERE sensorType = ? ORDER BY accessTime DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String = __converters.fromSensorType(sensorType)
        _stmt.bindText(_argIndex, _tmp)
        _argIndex = 2
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfSensorType: Int = getColumnIndexOrThrow(_stmt, "sensorType")
        val _columnIndexOfAccessTime: Int = getColumnIndexOrThrow(_stmt, "accessTime")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _columnIndexOfWasInBackground: Int = getColumnIndexOrThrow(_stmt, "wasInBackground")
        val _columnIndexOfWasScreenOff: Int = getColumnIndexOrThrow(_stmt, "wasScreenOff")
        val _columnIndexOfIsSuspicious: Int = getColumnIndexOrThrow(_stmt, "isSuspicious")
        val _columnIndexOfSuspiciousReason: Int = getColumnIndexOrThrow(_stmt, "suspiciousReason")
        val _result: MutableList<SensorAccessLog> = mutableListOf()
        while (_stmt.step()) {
          val _item: SensorAccessLog
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpSensorType: SensorType
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfSensorType)
          _tmpSensorType = __converters.toSensorType(_tmp_1)
          val _tmpAccessTime: Long
          _tmpAccessTime = _stmt.getLong(_columnIndexOfAccessTime)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          val _tmpWasInBackground: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfWasInBackground).toInt()
          _tmpWasInBackground = _tmp_2 != 0
          val _tmpWasScreenOff: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfWasScreenOff).toInt()
          _tmpWasScreenOff = _tmp_3 != 0
          val _tmpIsSuspicious: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsSuspicious).toInt()
          _tmpIsSuspicious = _tmp_4 != 0
          val _tmpSuspiciousReason: String?
          if (_stmt.isNull(_columnIndexOfSuspiciousReason)) {
            _tmpSuspiciousReason = null
          } else {
            _tmpSuspiciousReason = _stmt.getText(_columnIndexOfSuspiciousReason)
          }
          _item = SensorAccessLog(_tmpId,_tmpPackageName,_tmpAppName,_tmpSensorType,_tmpAccessTime,_tmpDuration,_tmpWasInBackground,_tmpWasScreenOff,_tmpIsSuspicious,_tmpSuspiciousReason)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSuspiciousLogs(limit: Int): List<SensorAccessLog> {
    val _sql: String = "SELECT * FROM sensor_access_logs WHERE isSuspicious = 1 ORDER BY accessTime DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfSensorType: Int = getColumnIndexOrThrow(_stmt, "sensorType")
        val _columnIndexOfAccessTime: Int = getColumnIndexOrThrow(_stmt, "accessTime")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _columnIndexOfWasInBackground: Int = getColumnIndexOrThrow(_stmt, "wasInBackground")
        val _columnIndexOfWasScreenOff: Int = getColumnIndexOrThrow(_stmt, "wasScreenOff")
        val _columnIndexOfIsSuspicious: Int = getColumnIndexOrThrow(_stmt, "isSuspicious")
        val _columnIndexOfSuspiciousReason: Int = getColumnIndexOrThrow(_stmt, "suspiciousReason")
        val _result: MutableList<SensorAccessLog> = mutableListOf()
        while (_stmt.step()) {
          val _item: SensorAccessLog
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpSensorType: SensorType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfSensorType)
          _tmpSensorType = __converters.toSensorType(_tmp)
          val _tmpAccessTime: Long
          _tmpAccessTime = _stmt.getLong(_columnIndexOfAccessTime)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          val _tmpWasInBackground: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfWasInBackground).toInt()
          _tmpWasInBackground = _tmp_1 != 0
          val _tmpWasScreenOff: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfWasScreenOff).toInt()
          _tmpWasScreenOff = _tmp_2 != 0
          val _tmpIsSuspicious: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsSuspicious).toInt()
          _tmpIsSuspicious = _tmp_3 != 0
          val _tmpSuspiciousReason: String?
          if (_stmt.isNull(_columnIndexOfSuspiciousReason)) {
            _tmpSuspiciousReason = null
          } else {
            _tmpSuspiciousReason = _stmt.getText(_columnIndexOfSuspiciousReason)
          }
          _item = SensorAccessLog(_tmpId,_tmpPackageName,_tmpAppName,_tmpSensorType,_tmpAccessTime,_tmpDuration,_tmpWasInBackground,_tmpWasScreenOff,_tmpIsSuspicious,_tmpSuspiciousReason)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getLogsSince(startTime: Long): List<SensorAccessLog> {
    val _sql: String = "SELECT * FROM sensor_access_logs WHERE accessTime >= ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startTime)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfSensorType: Int = getColumnIndexOrThrow(_stmt, "sensorType")
        val _columnIndexOfAccessTime: Int = getColumnIndexOrThrow(_stmt, "accessTime")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _columnIndexOfWasInBackground: Int = getColumnIndexOrThrow(_stmt, "wasInBackground")
        val _columnIndexOfWasScreenOff: Int = getColumnIndexOrThrow(_stmt, "wasScreenOff")
        val _columnIndexOfIsSuspicious: Int = getColumnIndexOrThrow(_stmt, "isSuspicious")
        val _columnIndexOfSuspiciousReason: Int = getColumnIndexOrThrow(_stmt, "suspiciousReason")
        val _result: MutableList<SensorAccessLog> = mutableListOf()
        while (_stmt.step()) {
          val _item: SensorAccessLog
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpSensorType: SensorType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfSensorType)
          _tmpSensorType = __converters.toSensorType(_tmp)
          val _tmpAccessTime: Long
          _tmpAccessTime = _stmt.getLong(_columnIndexOfAccessTime)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          val _tmpWasInBackground: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfWasInBackground).toInt()
          _tmpWasInBackground = _tmp_1 != 0
          val _tmpWasScreenOff: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfWasScreenOff).toInt()
          _tmpWasScreenOff = _tmp_2 != 0
          val _tmpIsSuspicious: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsSuspicious).toInt()
          _tmpIsSuspicious = _tmp_3 != 0
          val _tmpSuspiciousReason: String?
          if (_stmt.isNull(_columnIndexOfSuspiciousReason)) {
            _tmpSuspiciousReason = null
          } else {
            _tmpSuspiciousReason = _stmt.getText(_columnIndexOfSuspiciousReason)
          }
          _item = SensorAccessLog(_tmpId,_tmpPackageName,_tmpAppName,_tmpSensorType,_tmpAccessTime,_tmpDuration,_tmpWasInBackground,_tmpWasScreenOff,_tmpIsSuspicious,_tmpSuspiciousReason)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCountBySensorSince(sensorType: SensorType, startTime: Long): Int {
    val _sql: String = "SELECT COUNT(*) FROM sensor_access_logs WHERE sensorType = ? AND accessTime >= ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String = __converters.fromSensorType(sensorType)
        _stmt.bindText(_argIndex, _tmp)
        _argIndex = 2
        _stmt.bindLong(_argIndex, startTime)
        val _result: Int
        if (_stmt.step()) {
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(0).toInt()
          _result = _tmp_1
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBackgroundCountSince(startTime: Long): Int {
    val _sql: String = "SELECT COUNT(*) FROM sensor_access_logs WHERE wasInBackground = 1 AND accessTime >= ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startTime)
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

  public override suspend fun getUniqueAppsCountBySensorSince(sensorType: SensorType, startTime: Long): Int {
    val _sql: String = "SELECT COUNT(DISTINCT packageName) FROM sensor_access_logs WHERE sensorType = ? AND accessTime >= ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String = __converters.fromSensorType(sensorType)
        _stmt.bindText(_argIndex, _tmp)
        _argIndex = 2
        _stmt.bindLong(_argIndex, startTime)
        val _result: Int
        if (_stmt.step()) {
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(0).toInt()
          _result = _tmp_1
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRecentLogsFlow(limit: Int): Flow<List<SensorAccessLog>> {
    val _sql: String = "SELECT * FROM sensor_access_logs ORDER BY accessTime DESC LIMIT ?"
    return createFlow(__db, false, arrayOf("sensor_access_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfSensorType: Int = getColumnIndexOrThrow(_stmt, "sensorType")
        val _columnIndexOfAccessTime: Int = getColumnIndexOrThrow(_stmt, "accessTime")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _columnIndexOfWasInBackground: Int = getColumnIndexOrThrow(_stmt, "wasInBackground")
        val _columnIndexOfWasScreenOff: Int = getColumnIndexOrThrow(_stmt, "wasScreenOff")
        val _columnIndexOfIsSuspicious: Int = getColumnIndexOrThrow(_stmt, "isSuspicious")
        val _columnIndexOfSuspiciousReason: Int = getColumnIndexOrThrow(_stmt, "suspiciousReason")
        val _result: MutableList<SensorAccessLog> = mutableListOf()
        while (_stmt.step()) {
          val _item: SensorAccessLog
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpSensorType: SensorType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfSensorType)
          _tmpSensorType = __converters.toSensorType(_tmp)
          val _tmpAccessTime: Long
          _tmpAccessTime = _stmt.getLong(_columnIndexOfAccessTime)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          val _tmpWasInBackground: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfWasInBackground).toInt()
          _tmpWasInBackground = _tmp_1 != 0
          val _tmpWasScreenOff: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfWasScreenOff).toInt()
          _tmpWasScreenOff = _tmp_2 != 0
          val _tmpIsSuspicious: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsSuspicious).toInt()
          _tmpIsSuspicious = _tmp_3 != 0
          val _tmpSuspiciousReason: String?
          if (_stmt.isNull(_columnIndexOfSuspiciousReason)) {
            _tmpSuspiciousReason = null
          } else {
            _tmpSuspiciousReason = _stmt.getText(_columnIndexOfSuspiciousReason)
          }
          _item = SensorAccessLog(_tmpId,_tmpPackageName,_tmpAppName,_tmpSensorType,_tmpAccessTime,_tmpDuration,_tmpWasInBackground,_tmpWasScreenOff,_tmpIsSuspicious,_tmpSuspiciousReason)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteOldLogs(beforeTime: Long) {
    val _sql: String = "DELETE FROM sensor_access_logs WHERE accessTime < ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, beforeTime)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllLogs() {
    val _sql: String = "DELETE FROM sensor_access_logs"
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
