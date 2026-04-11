package com.fiorella.plantguardian.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fiorella.plantguardian.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Esto llamará a tu XML del jardín
    }
}