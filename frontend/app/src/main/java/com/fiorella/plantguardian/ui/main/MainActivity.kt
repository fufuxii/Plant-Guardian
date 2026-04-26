package com.fiorella.plantguardian.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Inicializar la fecha y el clima
        actualizarFecha()
        cargarClima("Madrid") // Por ahora ponemos una ciudad fija para probar

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    findViewById<CardView>(R.id.cvWeather).visibility = android.view.View.VISIBLE
                    true
                }
                R.id.nav_plants -> {
                    findViewById<CardView>(R.id.cvWeather).visibility = android.view.View.GONE
                    Toast.makeText(this, "Mis plantas", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun cargar_clima(ubicacion: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.clima(ubicacion)
                if (response.isSuccessful && response.body() != null) {
                    val clima = response.body()!!
                    findViewById<TextView>(R.id.tvTemperatura).text = "${clima.temp} °C"
                    findViewById<TextView>(R.id.tvVientoValor).text = "${clima.viento} m/s"
                    findViewById<TextView>(R.id.tvHumedadValor).text = "${clima.humedad}%"
                    val ivWeatherStatus = findViewById<ImageView>(R.id.ivClima)
                    ivWeatherStatus.load(clima.icono)
                }
            } catch (e: Exception) {
                Log.e("WeatherError", "Error al obtener clima: ${e.message}")
            }
        }
    }
}