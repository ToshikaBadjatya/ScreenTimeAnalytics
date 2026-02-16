package com.example.screentimeanalytics

import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.screentimeanalytics.analytics.Analytics
import com.example.screentimeanalytics.analytics.PersistentStorageType
import com.example.screentimeanalytics.analytics.SyncWorker
import com.example.screentimeanalytics.analytics.TimeUnit
import com.example.screentimeanalytics.globals.Globals
import com.example.screentimeanalytics.utils.AnalyticsScope
import kotlinx.coroutines.launch

object ScreenTimeAnalytics {
     var analytics: Analytics?=null

    fun init(context: Context,config: ScreenTimeConfig){
        Globals.screenTimeConfig=config
        analytics= Analytics.Builder()
            .setStorageType(PersistentStorageType.DATABASE)
            .build(context.applicationContext)
        AnalyticsScope.scope.launch {
            if(analytics!!.hasUnsyncedEvents()){
                analytics!!.syncEvents()
            }
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