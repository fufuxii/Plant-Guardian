package com.fiorella.plantguardian.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText // Cambiado de TextView a EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels // Usamos activityViewModels para refrescar el perfil
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.ui.account.LoginActivity
import com.fiorella.plantguardian.ui.main.MainActivity
import com.fiorella.plantguardian.ui.tools.models.UserViewModel

class UserSettingsFragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.ocultarNav()

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
        val userId = prefs.getString("user_id", "") ?: ""

        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etCorreo = view.findViewById<EditText>(R.id.etCorreo)
        val etCiudad = view.findViewById<EditText>(R.id.etCiudad)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCambios)

        setupUI(etNombre, etCorreo, etCiudad)
        viewModel.cargarDatosUsuario(userId)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val ciudad = etCiudad.text.toString().trim()

            if (nombre.isNotEmpty() && correo.isNotEmpty()) {
                // Llamamos a la función de guardado que crearemos en el ViewModel
                viewModel.actualizarDatosPerfil(userId, nombre, correo, ciudad) { exito ->
                    if (exito) {
                        Toast.makeText(context, "¡Perfil actualizado!", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack() // Volvemos atrás al terminar
                    } else {
                        Toast.makeText(context, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "El nombre y el correo son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            cerrarSesion()
        }
    }

    private fun setupUI(etNombre: EditText, etCorreo: EditText, etCiudad: EditText) {
        listOf(etNombre, etCorreo, etCiudad).forEach {
            it.alpha = 0f
            it.translationX = -30f
        }

        viewModel.usuario.observe(viewLifecycleOwner) { user ->
            user?.let {
                etNombre.setText(it.nombre)
                etCorreo.setText(it.correo)
                etCiudad.setText(it.ubicacion)

                animarEntrada(etNombre, 0)
                animarEntrada(etCorreo, 100)
                animarEntrada(etCiudad, 200)
            }
        }
    }

    private fun animarEntrada(view: View, delay: Long) {
        view.animate()
            .alpha(1f)
            .translationX(0f)
            .setStartDelay(delay)
            .setDuration(350)
            .start()
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