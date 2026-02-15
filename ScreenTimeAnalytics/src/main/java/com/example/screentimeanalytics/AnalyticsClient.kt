package com.example.screentimeanalytics

interface AnalyticsClient {
    suspend fun sendEvent()
}