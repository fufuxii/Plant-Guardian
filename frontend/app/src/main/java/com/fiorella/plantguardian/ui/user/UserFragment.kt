package com.fiorella.plantguardian.ui.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.ui.main.MainActivity
import com.fiorella.plantguardian.ui.tools.adapters.AchievementAdapter
import com.fiorella.plantguardian.ui.tools.models.UserViewModel
import com.google.android.material.imageview.ShapeableImageView

class UserFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
        val userId = prefs.getString("user_id", "") ?: ""

        observarDatos(view)

        viewModel.cargarDatosUsuario(userId)
        viewModel.cargarLogrosUsuario(userId)
    }

    private fun observarDatos(view: View) {
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombreUsuario)
        val pNivel = view.findViewById<ProgressBar>(R.id.progressNivel)
        val tvNivel = view.findViewById<TextView>(R.id.tvNivel)
        val rvLogros = view.findViewById<RecyclerView>(R.id.rvLogros)

        rvLogros.layoutManager = GridLayoutManager(requireContext(), 3)

        viewModel.usuario.observe(viewLifecycleOwner) { user ->
            tvNombre.text = user.nombre
            tvNivel.text = "Nivel ${user.nivel}"
            pNivel.setProgress(user.progreso_porcentaje, true)

            ivAvatar.load(user.icono) {
                crossfade(true)
                placeholder(R.drawable.ic_user)
                error(R.drawable.ic_user)
                transformations(CircleCropTransformation())
            }
        }

        viewModel.logros.observe(viewLifecycleOwner) { listaLogros ->
            rvLogros.adapter = AchievementAdapter(listaLogros)
        }

        viewModel.cargando.observe(viewLifecycleOwner) { estaCargando ->

        }
    }
}