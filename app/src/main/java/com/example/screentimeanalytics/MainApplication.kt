package com.example.screentimeanalytics

import android.app.Application


class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ScreenTimeAnalytics.init(this)

    }
}