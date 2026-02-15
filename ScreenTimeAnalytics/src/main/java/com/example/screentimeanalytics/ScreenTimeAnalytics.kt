package com.example.screentimeanalytics

import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.screentimeanalytics.analytics.Analytics
import com.example.screentimeanalytics.analytics.TimeUnit

object ScreenTimeAnalytics {
    private lateinit var analytics: Analytics

    fun init(context: Context){
        analytics= Analytics.Builder().showTimes(true)
            .timeUnit(TimeUnit.MINUTES).showPercentage(true)
            .build()
        if(analytics.hasUnsyncedEvents()){
            analytics.syncEvents()
        }

        (context.applicationContext as? Application)?.let {
            val observer = AppLifecycleObserver(
                onForeground = {
                },
                onBackground = {
                   analytics.triggerSyncEvents()
                }
            )

            ProcessLifecycleOwner.get()
                .lifecycle
                .addObserver(observer)
        }


    }
   private class AppLifecycleObserver(
        private val onForeground: () -> Unit,
        private val onBackground: () -> Unit
    ) : DefaultLifecycleObserver {

        override fun onStart(owner: LifecycleOwner) {
            onForeground()
        }

        override fun onStop(owner: LifecycleOwner) {
            onBackground()
        }
    }

}