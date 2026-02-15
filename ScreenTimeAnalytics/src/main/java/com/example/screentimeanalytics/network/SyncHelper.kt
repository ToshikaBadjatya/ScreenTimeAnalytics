package com.example.screentimeanalytics.network

import com.example.screentimeanalytics.AnalyticsClient
import com.example.screentimeanalytics.ScreenTimeObject
import com.example.screentimeanalytics.ScreenTimeResponse
import com.example.screentimeanalytics.analytics.TimeUnit
import com.example.screentimeanalytics.storage.event.Event
import java.util.UUID


class SyncHelper(
    val showTimes: Boolean,
    val timeUnit: TimeUnit,
    val showPercentage: Boolean,
    val analyticsClient: AnalyticsClient
) {
    suspend fun syncEvents(events: List<Event>) {
        val response=clubEventsTogether(events)
        analyticsClient.sendEvent(response)
    }
    fun clubEventsTogether(events: List<Event>): ScreenTimeResponse {

        val groupedByScreen = events.groupBy { it.className }

        val screenTimeObjects = groupedByScreen.map { (screenName, screenEvents) ->

            val intervals = screenEvents.map { it.interval.copy(duration = it.interval.duration.toTimeUnit(timeUnit)) }

            val totalTime = intervals.sumOf { it.duration }

            ScreenTimeObject(
                screenName = screenName,
                totalTime = totalTime.toTimeUnit(timeUnit),
                durations = intervals
            )
        }

        val grandTotal = screenTimeObjects.sumOf { it.totalTime }.toDouble()

        val screenTimePercents = if (grandTotal > 0&& showPercentage) {
            screenTimeObjects.associate {
                it.screenName to ((it.totalTime / grandTotal) * 100)
            }
        } else {
            emptyMap()
        }

        return ScreenTimeResponse(
            syncId = UUID.randomUUID().toString(),
            screenTimeObjects = if(showTimes) screenTimeObjects else emptyList(),
            screenTimePercents = screenTimePercents
        )
    }
    private fun Double.toTimeUnit(timeUnit: TimeUnit): Double {
        return when (timeUnit) {
            TimeUnit.SECONDS -> this / 1_000.0
            TimeUnit.MINUTES -> this / 60_000.0
            TimeUnit.HOURS -> this / 3_600_000.0
        }
    }

}