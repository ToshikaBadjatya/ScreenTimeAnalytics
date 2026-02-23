package com.example.screentimeanalytics

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await


class FirebaseAnalyticsClient: AnalyticsClient {
    private val database: FirebaseDatabase = Firebase.database
    private val endpoint = "analytics"
    
    override suspend fun sendEvent(response: ScreenTimeResponse) {
        try {
            Log.e("syncResponse","check $response")
            if(!Const.LOG_TO_FIREBASE){
                return
            }
            val ref = database.reference.child(endpoint).push()
            
            ref.setValue(response)
                .await()
            
        } catch (e: Exception) {
        }
    }
}