package com.example.screentimeanalytics

import com.example.screentimeanalytics.storage.event.Interval
import kotlinx.serialization.Serializable
@Serializable
data class ScreenTimeInterval(
    val startDate: String,
    val endDate: String,
    val startTime: String,
    val endTime: String,
    val duration: String
)

@Serializable
data class ScreenTimeResponse(
    val syncId: String,
    val screenTimeObjects: List<ScreenTimeObject>,
    val screenTimePercents: Map<String, Double>
)
@Serializable
data class ScreenTimeObject(
    val screenName: String,
    val totalTime: Double,
    val durations: List<ScreenTimeInterval>
)

interface AnalyticsClient {
    suspend fun sendEvent(response: ScreenTimeResponse)
}