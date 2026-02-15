package com.example.screentimeanalytics.storage

import android.util.Log
import com.example.screentimeanalytics.analytics.PersistentStorageType
import com.example.screentimeanalytics.storage.agent.FileStorageAgent
import com.example.screentimeanalytics.storage.agent.RoomStorageAgent
import com.example.screentimeanalytics.storage.event.Event


class StorageHelper(persistentStorageType: PersistentStorageType ) {
   private val agent=when(persistentStorageType){
        PersistentStorageType.ROOM-> RoomStorageAgent()
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

    fun hasUnsyncedEvents(): Boolean {
        return false
    }
}