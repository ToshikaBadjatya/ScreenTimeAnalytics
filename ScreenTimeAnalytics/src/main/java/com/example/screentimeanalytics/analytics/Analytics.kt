package com.example.screentimeanalytics.analytics

import android.content.Context
import android.util.Log
import com.example.screentimeanalytics.AnalyticsClient
import com.example.screentimeanalytics.network.DefaultAnalyticsClient
import com.example.screentimeanalytics.network.SyncHelper
import com.example.screentimeanalytics.storage.StorageHelper
import com.example.screentimeanalytics.storage.event.Event
import com.example.screentimeanalytics.utils.AnalyticsScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


enum class TimeUnit{
    SECONDS,
    MINUTES,
    HOURS
}
enum class PersistentStorageType{
    FILE_SYSTEM,
    DATABASE
}
class Analytics private constructor(
    val context: Context){

    private  val  storageHelper= StorageHelper(context)

    private val networkHelper= SyncHelper()


    suspend fun logEvent(event: Event){
        withContext(Dispatchers.IO){
            storageHelper.logEvent(event)
        }
    }

     fun syncEvents(){
         AnalyticsScope.scope.launch {

             networkHelper.syncEvents(storageHelper.getAllEvents())
             storageHelper. deleteAllEvents()
         }
    }
    suspend fun hasUnsyncedEvents(): Boolean{

       return storageHelper.hasUnsyncedEvents()
    }


    class Builder {

        fun build(context: Context): Analytics {
            return Analytics(
                context = context.applicationContext
            )
        }

    }

}