package com.example.screentimeanalytics.network

import com.example.screentimeanalytics.AnalyticsClient
import com.example.screentimeanalytics.ScreenTimeResponse

class DefaultAnalyticsClient: AnalyticsClient {
    override suspend fun sendEvent(response: ScreenTimeResponse) {

    }
}