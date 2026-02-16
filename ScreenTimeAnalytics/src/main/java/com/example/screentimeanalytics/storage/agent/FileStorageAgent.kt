package com.example.screentimeanalytics.storage.agent

import android.content.Context
import com.example.screentimeanalytics.storage.event.Event

class FileStorageAgent private constructor(applicationContext: Context) : StorageAgent {
    override suspend fun logEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllEvents() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllEvents(): List<Event> {
        TODO("Not yet implemented")
    }
    companion object {
        @Volatile
        private var instance: FileStorageAgent? = null

        fun getInstance(context: Context): FileStorageAgent {
            return instance ?: synchronized(this) {
                instance ?: FileStorageAgent(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
}