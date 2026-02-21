package com.example.screentimeanalytics.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object AnalyticsScope {
    val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )
}