package com.vsecurity.scanner.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.vsecurity.scanner.`data`.model.AlertType
import com.vsecurity.scanner.`data`.model.PrivacyAlert
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
public class PrivacyAlertDao_Impl(
  __db: RoomDatabase,
) : PrivacyAlertDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPrivacyAlert: EntityInsertAdapter<PrivacyAlert>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfPrivacyAlert = object : EntityInsertAdapter<PrivacyAlert>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `privacy_alerts` (`id`,`packageName`,`appName`,`alertType`,`sensorType`,`message`,`timestamp`,`wasAcknowledged`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PrivacyAlert) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.packageName)
        statement.bindText(3, entity.appName)
        val _tmp: String = __converters.fromAlertType(entity.alertType)
        statement.bindText(4, _tmp)
        val _tmp_1: String = __converters.fromSensorType(entity.sensorType)
        statement.bindText(5, _tmp_1)
        statement.bindText(6, entity.message)
        statement.bindLong(7, entity.timestamp)
        val _tmp_2: Int = if (entity.wasAcknowledged) 1 else 0
        statement.bindLong(8, _tmp_2.toLong())
      }
    }
  }

  public override suspend fun insertAlert(alert: PrivacyAlert): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfPrivacyAlert.insert(_connection, alert)
  }

  public override suspend fun getRecentAlerts(limit: Int): List<PrivacyAlert> {
    val _sql: String = "SELECT * FROM privacy_alerts ORDER BY timestamp DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfAlertType: Int = getColumnIndexOrThrow(_stmt, "alertType")
        val _columnIndexOfSensorType: Int = getColumnIndexOrThrow(_stmt, "sensorType")
        val _columnIndexOfMessage: Int = getColumnIndexOrThrow(_stmt, "message")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfWasAcknowledged: Int = getColumnIndexOrThrow(_stmt, "wasAcknowledged")
        val _result: MutableList<PrivacyAlert> = mutableListOf()
        while (_stmt.step()) {
          val _item: PrivacyAlert
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpAlertType: AlertType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfAlertType)
          _tmpAlertType = __converters.toAlertType(_tmp)
          val _tmpSensorType: SensorType
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfSensorType)
          _tmpSensorType = __converters.toSensorType(_tmp_1)
          val _tmpMessage: String
          _tmpMessage = _stmt.getText(_columnIndexOfMessage)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpWasAcknowledged: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfWasAcknowledged).toInt()
          _tmpWasAcknowledged = _tmp_2 != 0
          _item = PrivacyAlert(_tmpId,_tmpPackageName,_tmpAppName,_tmpAlertType,_tmpSensorType,_tmpMessage,_tmpTimestamp,_tmpWasAcknowledged)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRecentAlertsFlow(limit: Int): Flow<List<PrivacyAlert>> {
    val _sql: String = "SELECT * FROM privacy_alerts ORDER BY timestamp DESC LIMIT ?"
    return createFlow(__db, false, arrayOf("privacy_alerts")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfAlertType: Int = getColumnIndexOrThrow(_stmt, "alertType")
        val _columnIndexOfSensorType: Int = getColumnIndexOrThrow(_stmt, "sensorType")
        val _columnIndexOfMessage: Int = getColumnIndexOrThrow(_stmt, "message")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfWasAcknowledged: Int = getColumnIndexOrThrow(_stmt, "wasAcknowledged")
        val _result: MutableList<PrivacyAlert> = mutableListOf()
        while (_stmt.step()) {
          val _item: PrivacyAlert
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpAlertType: AlertType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfAlertType)
          _tmpAlertType = __converters.toAlertType(_tmp)
          val _tmpSensorType: SensorType
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfSensorType)
          _tmpSensorType = __converters.toSensorType(_tmp_1)
          val _tmpMessage: String
          _tmpMessage = _stmt.getText(_columnIndexOfMessage)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpWasAcknowledged: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfWasAcknowledged).toInt()
          _tmpWasAcknowledged = _tmp_2 != 0
          _item = PrivacyAlert(_tmpId,_tmpPackageName,_tmpAppName,_tmpAlertType,_tmpSensorType,_tmpMessage,_tmpTimestamp,_tmpWasAcknowledged)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getUnacknowledgedAlerts(): List<PrivacyAlert> {
    val _sql: String = "SELECT * FROM privacy_alerts WHERE wasAcknowledged = 0 ORDER BY timestamp DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfAppName: Int = getColumnIndexOrThrow(_stmt, "appName")
        val _columnIndexOfAlertType: Int = getColumnIndexOrThrow(_stmt, "alertType")
        val _columnIndexOfSensorType: Int = getColumnIndexOrThrow(_stmt, "sensorType")
        val _columnIndexOfMessage: Int = getColumnIndexOrThrow(_stmt, "message")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfWasAcknowledged: Int = getColumnIndexOrThrow(_stmt, "wasAcknowledged")
        val _result: MutableList<PrivacyAlert> = mutableListOf()
        while (_stmt.step()) {
          val _item: PrivacyAlert
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpAppName: String
          _tmpAppName = _stmt.getText(_columnIndexOfAppName)
          val _tmpAlertType: AlertType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfAlertType)
          _tmpAlertType = __converters.toAlertType(_tmp)
          val _tmpSensorType: SensorType
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfSensorType)
          _tmpSensorType = __converters.toSensorType(_tmp_1)
          val _tmpMessage: String
          _tmpMessage = _stmt.getText(_columnIndexOfMessage)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpWasAcknowledged: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfWasAcknowledged).toInt()
          _tmpWasAcknowledged = _tmp_2 != 0
          _item = PrivacyAlert(_tmpId,_tmpPackageName,_tmpAppName,_tmpAlertType,_tmpSensorType,_tmpMessage,_tmpTimestamp,_tmpWasAcknowledged)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getUnacknowledgedCount(): Int {
    val _sql: String = "SELECT COUNT(*) FROM privacy_alerts WHERE wasAcknowledged = 0"
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

  public override suspend fun getAlertCountInRange(startTime: Long, endTime: Long): Int {
    val _sql: String = "SELECT COUNT(*) FROM privacy_alerts WHERE timestamp >= ? AND timestamp < ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startTime)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endTime)
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

  public override suspend fun acknowledgeAlert(alertId: Long) {
    val _sql: String = "UPDATE privacy_alerts SET wasAcknowledged = 1 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, alertId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun acknowledgeAllAlerts() {
    val _sql: String = "UPDATE privacy_alerts SET wasAcknowledged = 1"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteOldAlerts(beforeTime: Long) {
    val _sql: String = "DELETE FROM privacy_alerts WHERE timestamp < ?"
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

  public override suspend fun deleteAllAlerts() {
    val _sql: String = "DELETE FROM privacy_alerts"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAlert(alertId: Long) {
    val _sql: String = "DELETE FROM privacy_alerts WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, alertId)
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
