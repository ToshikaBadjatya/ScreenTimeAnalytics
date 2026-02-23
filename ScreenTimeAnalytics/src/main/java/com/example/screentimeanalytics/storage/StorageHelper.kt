package com.example.screentimeanalytics.storage

import android.content.Context
import android.util.Log
import com.example.screentimeanalytics.analytics.PersistentStorageType
import com.example.screentimeanalytics.globals.Globals
import com.example.screentimeanalytics.storage.agent.filesystem.FileStorageAgent
import com.example.screentimeanalytics.storage.agent.database.DatabaseStorageAgent
import com.example.screentimeanalytics.storage.event.Event


class StorageHelper( context: Context) {
    private val agent=when(Globals.screenTimeConfig!!.storageType){
        PersistentStorageType.DATABASE-> DatabaseStorageAgent.getInstance(context)
        else -> FileStorageAgent.getInstance(context.applicationContext)
    }
    suspend fun logEvent(event: Event) {
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