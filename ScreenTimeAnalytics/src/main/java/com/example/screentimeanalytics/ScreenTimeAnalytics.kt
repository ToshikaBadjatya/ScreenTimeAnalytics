package com.example.screentimeanalytics

import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.screentimeanalytics.analytics.Analytics
import com.example.screentimeanalytics.analytics.PersistentStorageType
import com.example.screentimeanalytics.analytics.SyncWorker
import com.example.screentimeanalytics.analytics.TimeUnit

object ScreenTimeAnalytics {
    private lateinit var analytics: Analytics
    private lateinit var screenTimeConfig: ScreenTimeConfig

    fun init(context: Context,config: ScreenTimeConfig){
        screenTimeConfig=config
        analytics= Analytics.Builder().showTimes(config.showTimes)
            .timeUnit(config.timeUnit).showPercentage(config.showPercentage)
            .setStorageType(PersistentStorageType.ROOM)
            .setAnalyticsClient(config.analyticsClient)
            .build()
        if(analytics.hasUnsyncedEvents()){
            analytics.syncEvents()
        }

        (context.applicationContext as? Application)?.let {application ->
            val observer = AppLifecycleObserver(
                onForeground = {
                },
                onBackground = {
                   triggerSyncEvents(application)
                }
            )

            ProcessLifecycleOwner.get()
                .lifecycle
                .addObserver(observer)
        }


    }

    private fun triggerSyncEvents(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueue(myWorkRequest)

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