package com.fiorella.plantguardian.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fiorella.plantguardian.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_plants -> {
                    Toast.makeText(this, "Mis plantas", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_add -> {
                    Toast.makeText(this, "Añadir planta", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Mi perfil", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}