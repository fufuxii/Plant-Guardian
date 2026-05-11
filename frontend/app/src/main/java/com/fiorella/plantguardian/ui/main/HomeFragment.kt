package com.fiorella.plantguardian.ui.main

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.ui.tools.models.UserViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()

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
            setupClima(view, userId, prefs)
            setupPerfilUsuario(view, userId)
            setupAccionesRapidas(view)
        }
    }

    private fun setupPerfilUsuario(view: View, userId: String) {
        val tvNombre = view.findViewById<TextView>(R.id.tvSaludoNombre)
        val tvXp = view.findViewById<TextView>(R.id.tvXP)
        val pbNivel = view.findViewById<ProgressBar>(R.id.pbNivel)
        val tvNivelLabel = view.findViewById<TextView>(R.id.tvNivel)
        val ivUser = view.findViewById<ImageView>(R.id.ivIconoUsuarioHome)

        userViewModel.cargarDatosUsuario(userId)

        userViewModel.usuario.observe(viewLifecycleOwner) { user ->
            user?.let {
                tvNombre.text = it.nombre
                tvNivelLabel.text = "Nivel ${it.nivel}"
                tvXp.text = "${it.experiencia_actual} / ${it.experiencia_nivel} xp"
                pbNivel.progress = it.progreso_porcentaje

                ivUser?.load(it.icono) {
                    transformations(CircleCropTransformation())
                    error(R.drawable.ic_user)
                }
            }
        }
    }

    private fun setupAccionesRapidas(view: View) {
        view.findViewById<LinearLayout>(R.id.llAccionMisplantas).setOnClickListener {
            (activity as? MainActivity)?.asignarSeleccionMenu(R.id.nav_plants)
        }

        view.findViewById<LinearLayout>(R.id.llAccionAnadir).setOnClickListener {
            (activity as? MainActivity)?.asignarSeleccionMenu(R.id.nav_add)
        }

        view.findViewById<LinearLayout>(R.id.llUsuarioInfo).setOnClickListener {
            (activity as? MainActivity)?.asignarSeleccionMenu(R.id.nav_profile)
        }
    }

    private fun setupClima(view: View, userId: String, prefs: android.content.SharedPreferences) {
        val ultimaCarga = prefs.getLong("clima_timestamp", 0L)
        val ahora = System.currentTimeMillis()
        if (ahora - ultimaCarga > 30 * 60 * 1000) {
            cargarClima(userId, view, prefs)
        } else {
            mostrarClima(view, prefs)
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
                Log.e("DEBUG_HOME", "Error Clima: ${e.message}")
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