package com.example.screentimeanalytics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.screentimeanalytics.storage.event.Event
import com.example.screentimeanalytics.utils.AnalyticsScope
import kotlinx.coroutines.launch

open class ScreenTimeActivity : AppCompatActivity() {
    private var event: Event.Builder?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        event= Event.Builder(getClassName()).apply {
            start()
        }
    }

    override fun onPause() {
        super.onPause()
        if(event==null){
            return
        }
        event!!.stop()
        AnalyticsScope.scope.launch {
            ScreenTimeAnalytics.analytics?.let {
                it.logEvent(event!!.build())
                event=null
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if(event!=null&&event!!.isRunning()){
            event!!.stop()
            AnalyticsScope.scope.launch {
                ScreenTimeAnalytics.analytics?.let {
                    it.logEvent(event!!.build())
                    event=null
                }
            }
        }
    }
    fun getClassName(): String=this::class.simpleName?:""


}