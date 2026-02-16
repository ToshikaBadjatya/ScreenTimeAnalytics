package com.example.screentimeanalytics.storage

import android.content.Context
import android.util.Log
import com.example.screentimeanalytics.analytics.PersistentStorageType
import com.example.screentimeanalytics.storage.agent.FileStorageAgent
import com.example.screentimeanalytics.storage.agent.database.DatabaseStorageAgent
import com.example.screentimeanalytics.storage.event.Event


class StorageHelper(persistentStorageType: PersistentStorageType ,context: Context) {
    private val agent=when(persistentStorageType){
        PersistentStorageType.DATABASE-> DatabaseStorageAgent(context)
        else -> FileStorageAgent()
    }
    suspend fun logEvent(event: Event) {
        Log.e("EventCheck","${event.className} ${event.interval.duration}")
       agent.logEvent(event)
    }
    suspend fun getAllEvents(): List<Event>{
        return agent.getAllEvents()
    }
    suspend fun deleteAllEvents(){
         agent.deleteAllEvents()
    }

    suspend fun hasUnsyncedEvents(): Boolean {
        return agent.getAllEvents().isNotEmpty()
    }
}