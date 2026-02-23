package com.example.screentimeanalytics.storage.agent.filesystem

import android.content.Context
import android.util.Log
import com.example.screentimeanalytics.storage.agent.StorageAgent
import com.example.screentimeanalytics.storage.event.Event
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class FileStorageAgent private constructor(val context: Context) : StorageAgent {
    
    private val gson = Gson()
    private val fileName = "config.txt"
    
    override suspend fun logEvent(event: Event) {
        try {
            convertToString(event)?.let {
                writeToFile(it)
            }
        } catch (e: IOException) {
        }
    }

    override suspend fun deleteAllEvents() {
        try {
            context.deleteFile(fileName)
        } catch (e: Exception) {
        }
    }

    override suspend fun getAllEvents(): List<Event> {
        try {
           return readFromFile(context).split("\n").map{
               convertToObject(it)
           }
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        } catch (e: Exception) {
        }

        return emptyList()
    }
    
    private suspend fun writeToFile(data: String) {
        try {
            val outputStreamWriter =
                OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_APPEND))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
        }
    }
    
    private suspend fun readFromFile(context: Context): String {
        var ret = ""

        try {
            val inputStream: InputStream? = context.openFileInput(fileName)

            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString: String? = ""
                val stringBuilder = StringBuilder()

                while ((bufferedReader.readLine().also { receiveString = it }) != null) {
                    stringBuilder.append("\n").append(receiveString)
                }

                inputStream.close()
                ret = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }

        return ret
    }
    fun<T> convertToObject(jsonString: String):T{
        val type = object : TypeToken<List<Event>>() {}.type
        return gson.fromJson(jsonString, type)

    }
    fun<T> convertToString(obj:T): String?{
       return gson.toJson(obj)
    }
    
    companion object {
        @Volatile
        private var instance: FileStorageAgent? = null

        fun getInstance(context: Context): FileStorageAgent {
            return instance ?: synchronized(this) {
                instance ?: FileStorageAgent(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
}