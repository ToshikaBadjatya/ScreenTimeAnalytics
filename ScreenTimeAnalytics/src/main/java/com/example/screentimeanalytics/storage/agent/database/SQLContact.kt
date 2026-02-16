package com.example.screentimeanalytics.storage.agent.database

import android.provider.BaseColumns

object EventContract {

    object EventEntry : BaseColumns {
        const val TABLE_NAME = "events"

        const val COLUMN_CLASS_NAME = "class_name"
        const val COLUMN_START_TIME = "start_time"
        const val COLUMN_END_TIME = "end_time"
        const val COLUMN_DURATION = "duration"
    }

    const val SQL_CREATE_TABLE = """
        CREATE TABLE ${EventEntry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${EventEntry.COLUMN_CLASS_NAME} TEXT NOT NULL,
            ${EventEntry.COLUMN_START_TIME} INTEGER NOT NULL,
            ${EventEntry.COLUMN_END_TIME} INTEGER NOT NULL,
            ${EventEntry.COLUMN_DURATION} REAL NOT NULL
        )
    """

    const val SQL_DROP_TABLE =
        "DROP TABLE IF EXISTS ${EventEntry.TABLE_NAME}"
}
