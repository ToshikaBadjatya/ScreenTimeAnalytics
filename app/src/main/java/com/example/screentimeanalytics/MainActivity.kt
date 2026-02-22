package com.example.screentimeanalytics

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.annotations.TrackActivity

@TrackActivity
class MainActivity : ScreenTimeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val btnNext = findViewById<Button>(com.example.screentimeanalytics.R.id.btnNext)

        btnNext.setOnClickListener {
            val intent=Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}