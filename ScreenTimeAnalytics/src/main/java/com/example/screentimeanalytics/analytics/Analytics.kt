package com.example.screentimeanalytics.analytics


enum class TimeUnit{
    SECONDS,
    MINUTES,
    HOURS
}
class Analytics private constructor(  val showTimes: Boolean,
                                      val timeUnit: TimeUnit,
                                      val showPercentage: Boolean){
    class Builder {

        private var showTimes: Boolean = true
        private var timeUnit: TimeUnit = TimeUnit.SECONDS
        private var showPercentage: Boolean = false

        fun showTimes(value: Boolean) = apply {
            this.showTimes = value
        }

        fun timeUnit(unit: TimeUnit) = apply {
            this.timeUnit = unit
        }

        fun showPercentage(value: Boolean) = apply {
            this.showPercentage = value
        }

        fun build(): Analytics {
            return Analytics(
                showTimes = showTimes,
                timeUnit = timeUnit,
                showPercentage = showPercentage
            )
        }
    }



    fun logEvent(){

    }

    fun triggerSyncEvents(){

    }

    fun syncEvents(){

    }
    fun hasUnsyncedEvents(): Boolean{
       return false
    }
}