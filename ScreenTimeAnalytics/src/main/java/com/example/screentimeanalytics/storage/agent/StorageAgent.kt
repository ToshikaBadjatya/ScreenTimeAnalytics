package com.example.screentimeanalytics.storage.agent

import com.example.screentimeanalytics.storage.event.Event

interface StorageAgent {
    suspend fun logEvent(event: Event)

    suspend fun deleteAllEvents()

    suspend fun getAllEvents(): List<Event>
}