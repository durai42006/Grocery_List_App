package com.example.grozon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        val isDarkmode=sharedPreferences.getBoolean("nightMode",false)
        AppCompatDelegate.setDefaultNightMode(if(isDarkmode)AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO)



        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent= Intent(this,Login::class.java)
                startActivity(intent)
                finish()
            },5580)


    }
}