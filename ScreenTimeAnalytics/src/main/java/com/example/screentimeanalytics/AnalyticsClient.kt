package com.example.screentimeanalytics

import com.example.screentimeanalytics.storage.event.Interval


data class ScreenTimeResponse(
    val syncId: String,
    val screenTimeObjects: List<ScreenTimeObject>,
    val screenTimePercents: Map<String, Double>
)

data class ScreenTimeObject(
    val screenName: String,
    val totalTime: Double,
    val durations: List<Interval>
)

interface AnalyticsClient {
    suspend fun sendEvent(response: ScreenTimeResponse)
}