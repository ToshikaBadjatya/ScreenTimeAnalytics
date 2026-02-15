package com.example.screentimeanalytics.storage.agent

import com.example.screentimeanalytics.storage.event.Event

class RoomStorageAgent: StorageAgent {
    override suspend fun logEvent(event: Event) {
    }

    override suspend fun deleteAllEvents() {
    }

    override suspend fun getAllEvents(): List<Event> {
        return emptyList()
    }
}