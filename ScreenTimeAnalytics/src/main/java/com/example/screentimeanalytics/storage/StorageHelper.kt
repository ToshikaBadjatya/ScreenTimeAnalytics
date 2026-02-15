package com.example.screentimeanalytics.storage

import com.example.screentimeanalytics.analytics.PersistentStorageType
import com.example.screentimeanalytics.storage.agent.FileStorageAgent
import com.example.screentimeanalytics.storage.agent.RoomStorageAgent
import com.example.screentimeanalytics.storage.event.Event


class StorageHelper(persistentStorageType: PersistentStorageType ) {
    val agent=when(persistentStorageType){
        PersistentStorageType.ROOM-> RoomStorageAgent()
        else -> FileStorageAgent()
    }
    suspend fun logEvent(event: Event) {
       agent.logEvent(event)
    }

    fun hasUnsyncedEvents(): Boolean {
        return false
    }
}