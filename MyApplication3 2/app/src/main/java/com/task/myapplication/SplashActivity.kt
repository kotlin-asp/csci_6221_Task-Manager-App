package com.task.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Find the start button
        val startButton: Button = findViewById(R.id.start_button)

        // Set an OnClickListener on the start button
        startButton.setOnClickListener {
            // Create an intent to navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Optional: finish this activity so the user can't navigate back to it
            finish()
        }
    }
}
