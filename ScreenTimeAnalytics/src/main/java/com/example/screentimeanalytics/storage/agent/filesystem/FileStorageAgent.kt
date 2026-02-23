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
    var newLine="##@@$"
    
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
           val events= readFromFile(context).split(newLine).map{

               convertToObject<Event>(it)
           }.filter { it!=null } as List<Event>
            return events
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        } catch (e: Exception) {
        }

        return emptyList()
    }
    
    private suspend fun writeToFile(data: String) {
        try {
            val file = context.getFileStreamPath(fileName)
            val fileExists = file?.exists() == true && file.length() > 0
            
            val outputStreamWriter =
                OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_APPEND))
            
            if (fileExists) {
                outputStreamWriter.write(newLine)
            }
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("FileStorageAgent", "Failed to write to file: ${e.message}")
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
                    stringBuilder.append(newLine).append(receiveString)
                }

                inputStream.close()
                ret = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }

        return ret
    }
    fun<T> convertToObject(jsonString: String):T?{
        val type = object : TypeToken<Event>() {}.type
        return try {
            gson.fromJson(jsonString, type)
        }catch (e: java.lang.Exception){
            null
        }

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