package com.example.screentimeanalytics.storage.agent.database

import android.content.Context
import com.example.screentimeanalytics.storage.agent.StorageAgent
import com.example.screentimeanalytics.storage.event.Event

class DatabaseStorageAgent private constructor(
    context: Context
) : StorageAgent {

    private val dbHelper by lazy { DbHelper(context.applicationContext) }

    override suspend fun logEvent(event: Event) {
        dbHelper.insertIntoDb(event)
    }

    override suspend fun deleteAllEvents() {
        dbHelper.deleteAllEvents()
    }

    override suspend fun getAllEvents(): List<Event> {
        return dbHelper.getAllEvents()
    }

    companion object {
        @Volatile
        private var instance: DatabaseStorageAgent? = null

        fun getInstance(context: Context): DatabaseStorageAgent {
            return instance ?: synchronized(this) {
                instance ?: DatabaseStorageAgent(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
}
