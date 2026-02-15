package com.example.screentimeanalytics

import android.app.Application
import com.example.screentimeanalytics.analytics.TimeUnit


class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val config =
            ScreenTimeConfig.Builder()
                .showTimes(true)
                .showPercentage(true)
                .timeUnit(TimeUnit.SECONDS)
                .build()
        ScreenTimeAnalytics.init(this, config)

    }
}