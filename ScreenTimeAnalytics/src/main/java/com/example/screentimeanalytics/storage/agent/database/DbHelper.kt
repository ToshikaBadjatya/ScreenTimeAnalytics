package com.example.screentimeanalytics.storage.agent.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.screentimeanalytics.storage.event.Event
import com.example.screentimeanalytics.storage.event.Interval

class DbHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(EventContract.SQL_CREATE_TABLE)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }


    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL(EventContract.SQL_DROP_TABLE)
        onCreate(db)
    }
    fun insertIntoDb(event: Event){
        val db = writableDatabase

        val values = ContentValues().apply {
            put(EventContract.EventEntry.COLUMN_CLASS_NAME, event.className)
            put(EventContract.EventEntry.COLUMN_START_TIME, event.interval.startTime)
            put(EventContract.EventEntry.COLUMN_END_TIME, event.interval.endTime)
            put(EventContract.EventEntry.COLUMN_DURATION, event.interval.duration)
        }

// Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert(EventContract.EventEntry.TABLE_NAME, null, values)
    }
    fun getAllEvents(): List<Event> {
        val events = mutableListOf<Event>()
        val db = readableDatabase

        val projection = arrayOf(
            EventContract.EventEntry.COLUMN_CLASS_NAME,
            EventContract.EventEntry.COLUMN_START_TIME,
            EventContract.EventEntry.COLUMN_END_TIME,
            EventContract.EventEntry.COLUMN_DURATION
        )

        val cursor = db.query(
            EventContract.EventEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            "${EventContract.EventEntry.COLUMN_START_TIME} ASC" // optional ordering
        )

        cursor.use { c ->
            val classNameIndex =
                c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CLASS_NAME)
            val startTimeIndex =
                c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_START_TIME)
            val endTimeIndex =
                c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_END_TIME)
            val durationIndex =
                c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_DURATION)

            while (c.moveToNext()) {
                val className = c.getString(classNameIndex)
                val startTime = c.getLong(startTimeIndex)
                val endTime = c.getLong(endTimeIndex)
                val duration = c.getDouble(durationIndex)

                val interval = Interval(
                    startTime = startTime,
                    endTime = endTime,
                    duration = duration
                )

                events.add(Event(className, interval))
            }
        }

        db.close()
        return events
    }

    fun deleteAllEvents() {
        val db=writableDatabase
        db.delete(EventContract.EventEntry.TABLE_NAME,null,null)
        db.close()
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "EventReader.db"
    }
}
