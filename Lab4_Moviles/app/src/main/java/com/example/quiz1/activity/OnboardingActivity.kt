package com.example.quiz1.activity

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz1.R

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE)
        if (prefs.getBoolean("onboarding_complete", false)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_onboarding)

        findViewById<Button>(R.id.btnContinuar).setOnClickListener {
            prefs.edit().putBoolean("onboarding_complete", true).apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
