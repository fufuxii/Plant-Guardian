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

class InicioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", MODE_PRIVATE)
        val userId = prefs.getString("user_id", null)
        if (userId != null) {
            actualizar_fecha(view)
            cargar_clima(userId, view)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun cargar_clima(userId: String, view: View) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.obtener_clima(userId)
                if (response.isSuccessful && response.body() != null) {
                    val clima = response.body()!!

                    view.findViewById<TextView>(R.id.tvTemperatura).text = "${clima.temp.toInt()}°C"
                    view.findViewById<TextView>(R.id.tvVientoValor).text = "${clima.viento} m/s"
                    view.findViewById<TextView>(R.id.tvHumedadValor).text = "${clima.humedad} %"
                    view.findViewById<TextView>(R.id.tvLluviaValor).text = "${clima.lluvia} mm"
                    view.findViewById<ImageView>(R.id.ivClima).load(clima.icono)
                }
            } catch (e: Exception) {
                Log.e("DEBUG_PLANT", "Error de red: ${e.message}")
            }
        }
    }

    private fun actualizar_fecha(view: View) {
        val tvDate = view.findViewById<TextView>(R.id.tvFecha)
        val sdf = java.text.SimpleDateFormat("EEEE, d MMMM", java.util.Locale("es", "ES"))
        tvDate.text = sdf.format(java.util.Date()).replaceFirstChar { it.uppercase() }
    }
}