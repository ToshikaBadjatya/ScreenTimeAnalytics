package com.example.screentimeanalytics.analytics

import com.example.screentimeanalytics.AnalyticsClient
import com.example.screentimeanalytics.network.DefaultAnalyticsClient
import com.example.screentimeanalytics.network.SyncHelper
import com.example.screentimeanalytics.storage.StorageHelper
import com.example.screentimeanalytics.storage.event.Event


enum class TimeUnit{
    SECONDS,
    MINUTES,
    HOURS
}
enum class PersistentStorageType{
    FILE_SYSTEM,
    ROOM
}
class Analytics private constructor(  val showTimes: Boolean,
                                      val timeUnit: TimeUnit,
                                      val showPercentage: Boolean,
                                      val analyticsClient: AnalyticsClient,
    val storageType: PersistentStorageType){

    private  val  storageHelper= StorageHelper(storageType)

    private val networkHelper= SyncHelper(analyticsClient)

    suspend fun logEvent(event: Event){
        storageHelper.logEvent(event)
    }

    suspend fun syncEvents(){
      networkHelper.syncEvents()
    }
    fun hasUnsyncedEvents(): Boolean{
       return storageHelper.hasUnsyncedEvents()
    }


    //Builder

    class Builder {

        private  var analyticsClient: AnalyticsClient = DefaultAnalyticsClient()
        private var showTimes: Boolean = true
        private var timeUnit: TimeUnit = TimeUnit.SECONDS
        private var showPercentage: Boolean = false

        private var storageType: PersistentStorageType= PersistentStorageType.ROOM

        fun showTimes(value: Boolean) = apply {
            this.showTimes = value
        }

        fun timeUnit(unit: TimeUnit) = apply {
            this.timeUnit = unit
        }

        fun showPercentage(value: Boolean) = apply {
            this.showPercentage = value
        }
        fun setStorageType(storageType: PersistentStorageType) = apply {
            this.storageType = storageType
        }

        fun build(): Analytics {
            return Analytics(
                showTimes = showTimes,
                timeUnit = timeUnit,
                showPercentage = showPercentage,
                storageType=storageType,
                analyticsClient = analyticsClient
            )
        }

        fun setAnalyticsClient(analyticsClient: AnalyticsClient) {
            this.analyticsClient = analyticsClient
        }
    }

}