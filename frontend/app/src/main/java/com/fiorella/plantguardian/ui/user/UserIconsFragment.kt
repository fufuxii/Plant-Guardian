package com.fiorella.plantguardian.ui.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.ui.main.MainActivity
import com.fiorella.plantguardian.ui.tools.adapters.IconAdapter
import com.fiorella.plantguardian.ui.tools.models.UserViewModel
import kotlinx.coroutines.launch

class UserIconsFragment : Fragment() {
    private val viewModel: UserViewModel by activityViewModels()
    private var iconoElegido: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_icons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.ocultarNav()

        val rv = view.findViewById<RecyclerView>(R.id.rvIconos)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val userId = requireContext()
            .getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
            .getString("user_id", "") ?: ""

        rv.alpha = 0f
        rv.translationY = 40f

        rv.layoutManager = GridLayoutManager(requireContext(), 3)

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.obtenerIconosDisponibles(userId)
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    if (lista.isNotEmpty()) {
                        rv.adapter = IconAdapter(lista) { url -> iconoElegido = url }
                        rv.animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(400)
                            .start()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error al cargar iconos", Toast.LENGTH_SHORT).show()
            }
        }

        btnGuardar.setOnClickListener {
            if (iconoElegido != null) {
                actualizarIconoEnBD(userId, iconoElegido!!)
            } else {
                Toast.makeText(context, "Selecciona un icono", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarIconoEnBD(userId: String, url: String) {
        lifecycleScope.launch {
            try {
                val res = RetrofitClient.instance.actualizarIcono(userId, mapOf("url" to url))
                if (res.isSuccessful) {
                    viewModel.refrescarDatos(userId)
                    Toast.makeText(requireContext(), "¡Icono actualizado!", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                } else {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }
}