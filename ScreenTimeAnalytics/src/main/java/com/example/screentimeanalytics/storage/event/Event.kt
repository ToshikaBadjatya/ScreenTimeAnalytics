package com.example.screentimeanalytics.storage.event

data class Interval(
    val startTime: Long,
    val endTime: Long,
    val duration: Double
)
class Event (
    val className: String,
    val interval: Interval
) {

    class Builder(private val className: String) {

        private var startTime: Long? = null
        private var endTime: Long? = null

        fun start(): Builder = apply {
            startTime = System.currentTimeMillis()
            endTime=null
        }

        fun stop(): Builder = apply {
            endTime = System.currentTimeMillis()
        }
        fun isRunning(): Boolean{
            return endTime==null
        }

        fun build(): Event {
            val start = requireNotNull(startTime) {
                "start() must be called before build()"
            }

            val end = endTime ?: System.currentTimeMillis()

            val interval = Interval(
                startTime = start,
                endTime = end,
                duration = (end - start).toDouble()
            )

            return Event(
                className = className,
                interval = interval
            )
        }
    }
}
