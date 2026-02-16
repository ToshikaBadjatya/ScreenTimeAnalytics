package com.example.screentimeanalytics.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


    fun Long.formatDateAndTime(locale: Locale): Pair<String, String>{
        val dateFormat = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
        val dateString = dateFormat.format(Date(this))

// Format time as "HH:mm:ss"
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeString = timeFormat.format(Date(this))
        return Pair(dateString,timeString)
    }
