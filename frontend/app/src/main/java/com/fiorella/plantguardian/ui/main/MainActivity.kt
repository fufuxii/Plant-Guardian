package com.fiorella.plantguardian.ui.main
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.network.RetrofitClient
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = getSharedPreferences("PlantGuardianPrefs", MODE_PRIVATE)
        val userId = prefs.getString("user_id", null)
        if (userId != null) {
            actualizar_fecha()
            cargar_clima(userId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun cargar_clima(userId: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.obtener_clima(userId)
                if (response.isSuccessful && response.body() != null) {
                    val clima = response.body()!!
                    findViewById<TextView>(R.id.tvTemperatura).text = "${clima.temp.toInt()}°C"
                    findViewById<TextView>(R.id.tvVientoValor).text = "${clima.viento} m/s"
                    findViewById<TextView>(R.id.tvHumedadValor).text = "${clima.humedad} %"
                    findViewById<TextView>(R.id.tvLluviaValor).text = "${clima.lluvia} mm"
                    findViewById<ImageView>(R.id.ivClima).load(clima.icono)
                } else {
                    Log.e("DEBUG_PLANT", "Error en la respuesta del clima: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("DEBUG_PLANT", "Error de red: ${e.message}")
            }
        }
    }

    private fun actualizar_fecha() {
        val tvDate = findViewById<TextView>(R.id.tvFecha)
        val sdf = java.text.SimpleDateFormat("EEEE, d MMMM", java.util.Locale("es", "ES"))
        tvDate.text = sdf.format(java.util.Date()).replaceFirstChar { it.uppercase() }
    }
}