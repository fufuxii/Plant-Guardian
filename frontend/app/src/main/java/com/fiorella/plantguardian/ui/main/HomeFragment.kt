package com.fiorella.plantguardian.ui.main
import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.network.RetrofitClient
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", MODE_PRIVATE)
        val userId = prefs.getString("user_id", null)

        if (userId != null) {
            actualizarFecha(view)
            val ultimaCarga = prefs.getLong("clima_timestamp", 0L)
            val ahora = System.currentTimeMillis()
            val maxEspera = 30 * 60 * 1000
            if (ahora - ultimaCarga > maxEspera) {
                cargarClima(userId, view, prefs)
            } else {
                mostrarClima(view, prefs)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun cargarClima(userId: String, view: View, prefs: android.content.SharedPreferences) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.obtenerClima(userId)
                if (response.isSuccessful && response.body() != null) {
                    val clima = response.body()!!

                    actualizarClima(view, clima.temp, clima.viento, clima.humedad, clima.lluvia, clima.icono)
                    prefs.edit().apply {
                        putFloat("cache_temp", clima.temp.toFloat())
                        putString("cache_viento", clima.viento.toString())
                        putString("cache_humedad", clima.humedad.toString())
                        putString("cache_lluvia", clima.lluvia.toString())
                        putString("cache_icono", clima.icono)
                        putLong("clima_timestamp", System.currentTimeMillis())
                        apply()
                    }
                }
            } catch (e: Exception) {
                Log.e("DEBUG_PLANT", "Error de red: ${e.message}")
            }
        }
    }

    private fun mostrarClima(view: View, prefs: android.content.SharedPreferences) {
        val temp = prefs.getFloat("cache_temp", 0f).toDouble()
        val viento = prefs.getString("cache_viento", "0.0")?.toDoubleOrNull() ?: 0.0
        val humedad = prefs.getString("cache_humedad", "0")?.toIntOrNull() ?: 0
        val lluvia = prefs.getString("cache_lluvia", "0.0")?.toDoubleOrNull() ?: 0.0
        val icono = prefs.getString("cache_icono", "") ?: ""
        actualizarClima(view, temp, viento, humedad, lluvia, icono)
    }

    @SuppressLint("SetTextI18n")
    private fun actualizarClima(view: View, temp: Double, viento: Double, humedad: Int, lluvia: Double, icono: String) {
        view.findViewById<TextView>(R.id.tvTemperatura).text = "${temp.toInt()}°C"
        view.findViewById<TextView>(R.id.tvVientoValor).text = "$viento m/s"
        view.findViewById<TextView>(R.id.tvHumedadValor).text = "$humedad %"
        view.findViewById<TextView>(R.id.tvLluviaValor).text = "$lluvia mm"
        view.findViewById<ImageView>(R.id.ivClima).load(icono)
    }

    private fun actualizarFecha(view: View) {
        val tvDate = view.findViewById<TextView>(R.id.tvFecha)
        val sdf = java.text.SimpleDateFormat("EEEE, d MMMM", java.util.Locale("es", "ES"))
        tvDate.text = sdf.format(java.util.Date()).replaceFirstChar { it.uppercase() }
    }
}