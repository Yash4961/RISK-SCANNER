package com.vsecurity.scanner.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.vsecurity.scanner.`data`.model.DailySensorSummary
import javax.`annotation`.processing.Generated
import kotlin.Int
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
public class DailySummaryDao_Impl(
  __db: RoomDatabase,
) : DailySummaryDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDailySensorSummary: EntityInsertAdapter<DailySensorSummary>
  init {
    this.__db = __db
    this.__insertAdapterOfDailySensorSummary = object : EntityInsertAdapter<DailySensorSummary>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `daily_sensor_summary` (`date`,`totalCameraAccesses`,`totalMicrophoneAccesses`,`totalLocationAccesses`,`totalBackgroundAccesses`,`uniqueAppsUsingCamera`,`uniqueAppsUsingMicrophone`,`uniqueAppsUsingLocation`,`alertsTriggered`) VALUES (?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DailySensorSummary) {
        statement.bindText(1, entity.date)
        statement.bindLong(2, entity.totalCameraAccesses.toLong())
        statement.bindLong(3, entity.totalMicrophoneAccesses.toLong())
        statement.bindLong(4, entity.totalLocationAccesses.toLong())
        statement.bindLong(5, entity.totalBackgroundAccesses.toLong())
        statement.bindLong(6, entity.uniqueAppsUsingCamera.toLong())
        statement.bindLong(7, entity.uniqueAppsUsingMicrophone.toLong())
        statement.bindLong(8, entity.uniqueAppsUsingLocation.toLong())
        statement.bindLong(9, entity.alertsTriggered.toLong())
      }
    }
  }

  public override suspend fun insertOrUpdate(summary: DailySensorSummary): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfDailySensorSummary.insert(_connection, summary)
  }

  public override suspend fun getRecentSummaries(days: Int): List<DailySensorSummary> {
    val _sql: String = "SELECT * FROM daily_sensor_summary ORDER BY date DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, days.toLong())
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfTotalCameraAccesses: Int = getColumnIndexOrThrow(_stmt, "totalCameraAccesses")
        val _columnIndexOfTotalMicrophoneAccesses: Int = getColumnIndexOrThrow(_stmt, "totalMicrophoneAccesses")
        val _columnIndexOfTotalLocationAccesses: Int = getColumnIndexOrThrow(_stmt, "totalLocationAccesses")
        val _columnIndexOfTotalBackgroundAccesses: Int = getColumnIndexOrThrow(_stmt, "totalBackgroundAccesses")
        val _columnIndexOfUniqueAppsUsingCamera: Int = getColumnIndexOrThrow(_stmt, "uniqueAppsUsingCamera")
        val _columnIndexOfUniqueAppsUsingMicrophone: Int = getColumnIndexOrThrow(_stmt, "uniqueAppsUsingMicrophone")
        val _columnIndexOfUniqueAppsUsingLocation: Int = getColumnIndexOrThrow(_stmt, "uniqueAppsUsingLocation")
        val _columnIndexOfAlertsTriggered: Int = getColumnIndexOrThrow(_stmt, "alertsTriggered")
        val _result: MutableList<DailySensorSummary> = mutableListOf()
        while (_stmt.step()) {
          val _item: DailySensorSummary
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpTotalCameraAccesses: Int
          _tmpTotalCameraAccesses = _stmt.getLong(_columnIndexOfTotalCameraAccesses).toInt()
          val _tmpTotalMicrophoneAccesses: Int
          _tmpTotalMicrophoneAccesses = _stmt.getLong(_columnIndexOfTotalMicrophoneAccesses).toInt()
          val _tmpTotalLocationAccesses: Int
          _tmpTotalLocationAccesses = _stmt.getLong(_columnIndexOfTotalLocationAccesses).toInt()
          val _tmpTotalBackgroundAccesses: Int
          _tmpTotalBackgroundAccesses = _stmt.getLong(_columnIndexOfTotalBackgroundAccesses).toInt()
          val _tmpUniqueAppsUsingCamera: Int
          _tmpUniqueAppsUsingCamera = _stmt.getLong(_columnIndexOfUniqueAppsUsingCamera).toInt()
          val _tmpUniqueAppsUsingMicrophone: Int
          _tmpUniqueAppsUsingMicrophone = _stmt.getLong(_columnIndexOfUniqueAppsUsingMicrophone).toInt()
          val _tmpUniqueAppsUsingLocation: Int
          _tmpUniqueAppsUsingLocation = _stmt.getLong(_columnIndexOfUniqueAppsUsingLocation).toInt()
          val _tmpAlertsTriggered: Int
          _tmpAlertsTriggered = _stmt.getLong(_columnIndexOfAlertsTriggered).toInt()
          _item = DailySensorSummary(_tmpDate,_tmpTotalCameraAccesses,_tmpTotalMicrophoneAccesses,_tmpTotalLocationAccesses,_tmpTotalBackgroundAccesses,_tmpUniqueAppsUsingCamera,_tmpUniqueAppsUsingMicrophone,_tmpUniqueAppsUsingLocation,_tmpAlertsTriggered)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSummaryForDate(date: String): DailySensorSummary? {
    val _sql: String = "SELECT * FROM daily_sensor_summary WHERE date = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfTotalCameraAccesses: Int = getColumnIndexOrThrow(_stmt, "totalCameraAccesses")
        val _columnIndexOfTotalMicrophoneAccesses: Int = getColumnIndexOrThrow(_stmt, "totalMicrophoneAccesses")
        val _columnIndexOfTotalLocationAccesses: Int = getColumnIndexOrThrow(_stmt, "totalLocationAccesses")
        val _columnIndexOfTotalBackgroundAccesses: Int = getColumnIndexOrThrow(_stmt, "totalBackgroundAccesses")
        val _columnIndexOfUniqueAppsUsingCamera: Int = getColumnIndexOrThrow(_stmt, "uniqueAppsUsingCamera")
        val _columnIndexOfUniqueAppsUsingMicrophone: Int = getColumnIndexOrThrow(_stmt, "uniqueAppsUsingMicrophone")
        val _columnIndexOfUniqueAppsUsingLocation: Int = getColumnIndexOrThrow(_stmt, "uniqueAppsUsingLocation")
        val _columnIndexOfAlertsTriggered: Int = getColumnIndexOrThrow(_stmt, "alertsTriggered")
        val _result: DailySensorSummary?
        if (_stmt.step()) {
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpTotalCameraAccesses: Int
          _tmpTotalCameraAccesses = _stmt.getLong(_columnIndexOfTotalCameraAccesses).toInt()
          val _tmpTotalMicrophoneAccesses: Int
          _tmpTotalMicrophoneAccesses = _stmt.getLong(_columnIndexOfTotalMicrophoneAccesses).toInt()
          val _tmpTotalLocationAccesses: Int
          _tmpTotalLocationAccesses = _stmt.getLong(_columnIndexOfTotalLocationAccesses).toInt()
          val _tmpTotalBackgroundAccesses: Int
          _tmpTotalBackgroundAccesses = _stmt.getLong(_columnIndexOfTotalBackgroundAccesses).toInt()
          val _tmpUniqueAppsUsingCamera: Int
          _tmpUniqueAppsUsingCamera = _stmt.getLong(_columnIndexOfUniqueAppsUsingCamera).toInt()
          val _tmpUniqueAppsUsingMicrophone: Int
          _tmpUniqueAppsUsingMicrophone = _stmt.getLong(_columnIndexOfUniqueAppsUsingMicrophone).toInt()
          val _tmpUniqueAppsUsingLocation: Int
          _tmpUniqueAppsUsingLocation = _stmt.getLong(_columnIndexOfUniqueAppsUsingLocation).toInt()
          val _tmpAlertsTriggered: Int
          _tmpAlertsTriggered = _stmt.getLong(_columnIndexOfAlertsTriggered).toInt()
          _result = DailySensorSummary(_tmpDate,_tmpTotalCameraAccesses,_tmpTotalMicrophoneAccesses,_tmpTotalLocationAccesses,_tmpTotalBackgroundAccesses,_tmpUniqueAppsUsingCamera,_tmpUniqueAppsUsingMicrophone,_tmpUniqueAppsUsingLocation,_tmpAlertsTriggered)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSummaryFlow(date: String): Flow<DailySensorSummary?> {
    val _sql: String = "SELECT * FROM daily_sensor_summary WHERE date = ?"
    return createFlow(__db, false, arrayOf("daily_sensor_summary")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfTotalCameraAccesses: Int = getColumnIndexOrThrow(_stmt, "totalCameraAccesses")
        val _columnIndexOfTotalMicrophoneAccesses: Int = getColumnIndexOrThrow(_stmt, "totalMicrophoneAccesses")
        val _columnIndexOfTotalLocationAccesses: Int = getColumnIndexOrThrow(_stmt, "totalLocationAccesses")
        val _columnIndexOfTotalBackgroundAccesses: Int = getColumnIndexOrThrow(_stmt, "totalBackgroundAccesses")
        val _columnIndexOfUniqueAppsUsingCamera: Int = getColumnIndexOrThrow(_stmt, "uniqueAppsUsingCamera")
        val _columnIndexOfUniqueAppsUsingMicrophone: Int = getColumnIndexOrThrow(_stmt, "uniqueAppsUsingMicrophone")
        val _columnIndexOfUniqueAppsUsingLocation: Int = getColumnIndexOrThrow(_stmt, "uniqueAppsUsingLocation")
        val _columnIndexOfAlertsTriggered: Int = getColumnIndexOrThrow(_stmt, "alertsTriggered")
        val _result: DailySensorSummary?
        if (_stmt.step()) {
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpTotalCameraAccesses: Int
          _tmpTotalCameraAccesses = _stmt.getLong(_columnIndexOfTotalCameraAccesses).toInt()
          val _tmpTotalMicrophoneAccesses: Int
          _tmpTotalMicrophoneAccesses = _stmt.getLong(_columnIndexOfTotalMicrophoneAccesses).toInt()
          val _tmpTotalLocationAccesses: Int
          _tmpTotalLocationAccesses = _stmt.getLong(_columnIndexOfTotalLocationAccesses).toInt()
          val _tmpTotalBackgroundAccesses: Int
          _tmpTotalBackgroundAccesses = _stmt.getLong(_columnIndexOfTotalBackgroundAccesses).toInt()
          val _tmpUniqueAppsUsingCamera: Int
          _tmpUniqueAppsUsingCamera = _stmt.getLong(_columnIndexOfUniqueAppsUsingCamera).toInt()
          val _tmpUniqueAppsUsingMicrophone: Int
          _tmpUniqueAppsUsingMicrophone = _stmt.getLong(_columnIndexOfUniqueAppsUsingMicrophone).toInt()
          val _tmpUniqueAppsUsingLocation: Int
          _tmpUniqueAppsUsingLocation = _stmt.getLong(_columnIndexOfUniqueAppsUsingLocation).toInt()
          val _tmpAlertsTriggered: Int
          _tmpAlertsTriggered = _stmt.getLong(_columnIndexOfAlertsTriggered).toInt()
          _result = DailySensorSummary(_tmpDate,_tmpTotalCameraAccesses,_tmpTotalMicrophoneAccesses,_tmpTotalLocationAccesses,_tmpTotalBackgroundAccesses,_tmpUniqueAppsUsingCamera,_tmpUniqueAppsUsingMicrophone,_tmpUniqueAppsUsingLocation,_tmpAlertsTriggered)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteOldSummaries(beforeDate: String) {
    val _sql: String = "DELETE FROM daily_sensor_summary WHERE date < ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, beforeDate)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllSummaries() {
    val _sql: String = "DELETE FROM daily_sensor_summary"
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
