package com.fiorella.plantguardian.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.ui.account.LoginActivity
import com.fiorella.plantguardian.ui.main.MainActivity
import com.fiorella.plantguardian.ui.tools.models.UserViewModel

class UserSettingsFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.ocultarNav()

        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
        val userId = prefs.getString("user_id", "") ?: ""

        setupUI(view)
        viewModel.cargarDatosUsuario(userId)

        view.findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            cerrarSesion()
        }
    }

    private fun setupUI(view: View) {
        val etNombre = view.findViewById<TextView>(R.id.etNombre)
        val etCorreo = view.findViewById<TextView>(R.id.etCorreo)
        val etCiudad = view.findViewById<TextView>(R.id.etCiudad)

        viewModel.usuario.observe(viewLifecycleOwner) { user ->
            user?.let {
                etNombre.text = it.nombre
                etCorreo.text = it.correo
                etCiudad.text = it.ubicacion
            }
        }
    }

    private fun cerrarSesion() {
        val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }
}