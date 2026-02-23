package com.example.screentimeanalytics

import android.app.Application
import com.example.screentimeanalytics.analytics.PersistentStorageType
import com.example.screentimeanalytics.analytics.TimeUnit
import com.google.firebase.FirebaseApp

import java.util.Locale


class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val analyticsClient= FirebaseAnalyticsClient()
        val config =
            ScreenTimeConfig.Builder()
                .showTimes(true)
                .showPercentage(true)
                .timeUnit(TimeUnit.SECONDS)
                .analyticsClient(analyticsClient)
                .setLocale(Locale.getDefault())
                .setStorage(PersistentStorageType.DATABASE)
                .build()
        ScreenTimeAnalytics.init(this, config)

    }
}