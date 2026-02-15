package com.example.screentimeanalytics

import com.example.screentimeanalytics.analytics.TimeUnit
import com.example.screentimeanalytics.network.DefaultAnalyticsClient



data class ScreenTimeConfig private constructor(
    val analyticsClient: AnalyticsClient,
    val showTimes: Boolean,
    val showPercentage: Boolean,
    val timeUnit: TimeUnit,
) {
    class Builder {

        private var analyticsClient: AnalyticsClient = DefaultAnalyticsClient()
        private var showTimes: Boolean = true
        private var showPercentage: Boolean = true
        private var timeUnit: TimeUnit = TimeUnit.MINUTES

        fun analyticsClient(client: AnalyticsClient) = apply {
            this.analyticsClient = client
        }

        fun showTimes(value: Boolean) = apply {
            this.showTimes = value
        }

        fun showPercentage(value: Boolean) = apply {
            this.showPercentage = value
        }

        fun timeUnit(unit: TimeUnit) = apply {
            this.timeUnit = unit
        }

        fun build(): ScreenTimeConfig {
            require(!(showPercentage && !showTimes)) {
                "showPercentage requires showTimes to be enabled"
            }

            return ScreenTimeConfig(
                analyticsClient = analyticsClient,
                showTimes = showTimes,
                showPercentage = showPercentage,
                timeUnit = timeUnit,
            )
        }
    }
}
