package com.example.screentimeanalytics.network

import com.example.screentimeanalytics.AnalyticsClient
import com.example.screentimeanalytics.ScreenTimeObject
import com.example.screentimeanalytics.ScreenTimeResponse
import com.example.screentimeanalytics.storage.event.Event
import java.util.UUID


class SyncHelper(val analyticsClient: AnalyticsClient) {
    suspend fun syncEvents(events: List<Event>) {
        val response=clubEventsTogether(events)
        analyticsClient.sendEvent(response)
    }
    fun clubEventsTogether(events: List<Event>): ScreenTimeResponse {

        val groupedByScreen = events.groupBy { it.className }

        val screenTimeObjects = groupedByScreen.map { (screenName, screenEvents) ->

            val intervals = screenEvents.map { it.interval }

            val totalTime = intervals.sumOf { it.duration }

            ScreenTimeObject(
                screenName = screenName,
                totalTime = totalTime,
                durations = intervals
            )
        }

        val grandTotal = screenTimeObjects.sumOf { it.totalTime }.toDouble()

        val screenTimePercents = if (grandTotal > 0) {
            screenTimeObjects.associate {
                it.screenName to ((it.totalTime / grandTotal) * 100)
            }
        } else {
            emptyMap()
        }

        return ScreenTimeResponse(
            syncId = UUID.randomUUID().toString(),
            screenTimeObjects = screenTimeObjects,
            screenTimePercents = screenTimePercents
        )
    }

}