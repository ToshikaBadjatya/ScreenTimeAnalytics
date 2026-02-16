package com.example.screentimeanalytics.analytics

import android.content.Context
import android.util.Log
import com.example.screentimeanalytics.AnalyticsClient
import com.example.screentimeanalytics.network.DefaultAnalyticsClient
import com.example.screentimeanalytics.network.SyncHelper
import com.example.screentimeanalytics.storage.StorageHelper
import com.example.screentimeanalytics.storage.event.Event
import com.example.screentimeanalytics.utils.AnalyticsScope
import kotlinx.coroutines.launch
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
    val storageType: PersistentStorageType,
    val context: Context){

    private  val  storageHelper= StorageHelper(storageType,context)

    private val networkHelper= SyncHelper()


    suspend fun logEvent(event: Event){
        storageHelper.logEvent(event)
    }

     fun syncEvents(){
         Log.e("DefaultAnalyticsClient","sync called")
         AnalyticsScope.scope.launch {
             Log.e("DefaultAnalyticsClient","${storageHelper.getAllEvents()}")

             networkHelper.syncEvents(storageHelper.getAllEvents())
             storageHelper. deleteAllEvents()
         }
    }
    suspend fun hasUnsyncedEvents(): Boolean{

       return storageHelper.hasUnsyncedEvents()
    }


    class Builder {

        private var storageType: PersistentStorageType= PersistentStorageType.DATABASE

        fun setStorageType(storageType: PersistentStorageType) = apply {
            this.storageType = storageType
        }

        fun build(context: Context): Analytics {
            return Analytics(
                storageType=storageType,
                context = context.applicationContext
            )
        }

    }

}