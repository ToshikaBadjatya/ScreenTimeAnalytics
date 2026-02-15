package com.example.screentimeanalytics.analytics

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.screentimeanalytics.ScreenTimeAnalytics
import com.example.screentimeanalytics.storage.StorageHelper

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    var analytics: Analytics?=null

    override suspend fun doWork(): Result {
        return try {
         if(analytics==null){
             analytics = Analytics.Builder().showTimes(true)
                 .timeUnit(TimeUnit.MINUTES).showPercentage(true)
                 .setStorageType(PersistentStorageType.ROOM)
                 .build()
         }
            if(analytics!!.hasUnsyncedEvents()){
               analytics!!.syncEvents()
            }

            Result.success()
        } catch (e: Exception) {
            // Retry on failure
            Result.retry()
        }
    }
}