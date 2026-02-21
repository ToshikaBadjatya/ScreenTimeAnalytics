package com.example.screentimeanalytics

import android.app.Application
import com.example.screentimeanalytics.analytics.TimeUnit

import java.util.Locale


class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val analyticsClient= FirebaseAnalyticsClient()
        val config =
            ScreenTimeConfig.Builder()
                .showTimes(true)
                .showPercentage(true)
                .timeUnit(TimeUnit.SECONDS)
                .analyticsClient(analyticsClient)
                .build()
        ScreenTimeAnalytics.init(this, config)

    }
}