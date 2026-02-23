package com.example.screentimeanalytics.network

import android.util.Log
import com.example.screentimeanalytics.AnalyticsClient
import com.example.screentimeanalytics.ScreenTimeInterval
import com.example.screentimeanalytics.ScreenTimeObject
import com.example.screentimeanalytics.ScreenTimeResponse
import com.example.screentimeanalytics.analytics.TimeUnit
import com.example.screentimeanalytics.globals.Globals
import com.example.screentimeanalytics.storage.event.Event
import com.example.screentimeanalytics.storage.event.Interval
import com.example.screentimeanalytics.utils.formatDateAndTime
import java.util.Locale
import java.util.UUID


class SyncHelper() {
    suspend fun syncEvents(events: List<Event>) {

        if(Globals.screenTimeConfig==null){
            return
        }


        val response=clubEventsTogether(events)

        Globals.screenTimeConfig!!.analyticsClient.sendEvent(response)
    }
    fun clubEventsTogether(events: List<Event>): ScreenTimeResponse {

        val groupedByScreen = events.groupBy { it.className }

        val screenTimeObjects = groupedByScreen.map { (screenName, screenEvents) ->

            val intervals = screenEvents.map { it.interval.copy(duration = it.interval.duration.toTimeUnit(Globals.screenTimeConfig!!.timeUnit)) }

            val totalTime = intervals.sumOf { it.duration }

            ScreenTimeObject(
                screenName = screenName,
                totalTime = totalTime.toTimeUnit(Globals.screenTimeConfig!!.timeUnit),
                durations = intervals.map { it.toScreenTimeInterval(Globals.screenTimeConfig!!.locale,
                    Globals.screenTimeConfig!!.timeUnit) }
            )
        }

        val grandTotal = screenTimeObjects.sumOf { it.totalTime }.toDouble()

        val screenTimePercents = if (grandTotal > 0&& Globals.screenTimeConfig!!.showPercentage) {
            screenTimeObjects.associate {
                it.screenName to ((it.totalTime / grandTotal) * 100)
            }
        } else {
            emptyMap()
        }

        return ScreenTimeResponse(
            syncId = UUID.randomUUID().toString(),
            screenTimeObjects = if(Globals.screenTimeConfig!!.showTimes) screenTimeObjects else emptyList(),
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
    private fun Interval.toScreenTimeInterval(locale: Locale,timeUnit: TimeUnit): ScreenTimeInterval{
        val startNew=startTime.formatDateAndTime(locale)
        val endNew=endTime.formatDateAndTime(locale)
        val pred= when (timeUnit) {
            TimeUnit.SECONDS -> "s"
            TimeUnit.MINUTES ->"min"
            TimeUnit.HOURS -> "hr"
        }
        return ScreenTimeInterval(startNew.first,endNew.first,startNew.second,endNew.second,"${duration} ${pred}")
    }

}