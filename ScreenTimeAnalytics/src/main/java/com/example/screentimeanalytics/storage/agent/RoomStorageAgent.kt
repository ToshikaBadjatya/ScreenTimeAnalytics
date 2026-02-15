package com.example.screentimeanalytics.storage.agent

import com.example.screentimeanalytics.storage.event.Event

class RoomStorageAgent: StorageAgent {
    override suspend fun logEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllEvents() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllEvents(): List<Event> {
        TODO("Not yet implemented")
    }
}