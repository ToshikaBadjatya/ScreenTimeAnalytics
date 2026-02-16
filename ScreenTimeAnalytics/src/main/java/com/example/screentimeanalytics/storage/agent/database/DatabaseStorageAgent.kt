package com.example.screentimeanalytics.storage.agent.database

import android.content.Context
import com.example.screentimeanalytics.storage.agent.StorageAgent
import com.example.screentimeanalytics.storage.event.Event

class DatabaseStorageAgent(val context: Context): StorageAgent {
    private val dbHelper by lazy { DbHelper(context) }
    override suspend fun logEvent(event: Event) {
        dbHelper.insertIntoDb(event)
    }

    override suspend fun deleteAllEvents() {
        return dbHelper.deleteAllEvents()
    }

    override suspend fun getAllEvents(): List<Event> {
        return dbHelper.getAllEvents()
    }
}