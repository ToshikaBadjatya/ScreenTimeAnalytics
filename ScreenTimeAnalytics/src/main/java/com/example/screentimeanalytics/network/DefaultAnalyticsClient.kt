package com.example.screentimeanalytics.network

import android.util.Log
import com.example.screentimeanalytics.AnalyticsClient
import com.example.screentimeanalytics.ScreenTimeResponse

class DefaultAnalyticsClient: AnalyticsClient {
    override suspend fun sendEvent(response: ScreenTimeResponse) {
        Log.e("DefaultAnalyticsClient","$response")

    }
}