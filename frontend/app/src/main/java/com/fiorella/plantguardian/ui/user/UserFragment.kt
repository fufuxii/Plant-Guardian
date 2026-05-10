package com.fiorella.plantguardian.ui.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.AchievementData
import com.fiorella.plantguardian.ui.main.MainActivity
import com.fiorella.plantguardian.ui.tools.adapters.AchievementAdapter
import com.fiorella.plantguardian.ui.tools.models.UserViewModel

class UserFragment : Fragment() {
    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onResume() {
        super.onResume()
        val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
        val userId = prefs.getString("user_id", "") ?: ""

        if (userId.isNotEmpty()) {
            viewModel.sincronizarDatos(userId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
        val userId = prefs.getString("user_id", "") ?: ""

        setupBotones(view)
        setupObservadores(view, userId)
    }

    private fun setupBotones(view: View) {
        view.findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            (activity as? MainActivity)?.cargarFragmento(UserSettingsFragment(), "Ajustes", true)
        }
        view.findViewById<ImageButton>(R.id.btnEditAvatar).setOnClickListener {
            (activity as? MainActivity)?.cargarFragmento(UserIconsFragment(), "Iconos", true)
        }
    }

    private fun setupObservadores(view: View, userId: String) {
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombreUsuario)
        val pNivel = view.findViewById<ProgressBar>(R.id.progressNivel)
        val tvNivel = view.findViewById<TextView>(R.id.tvNivel)
        val rvLogros = view.findViewById<RecyclerView>(R.id.rvLogros)

        ocultarVistas(ivAvatar, tvNombre, tvNivel, pNivel, rvLogros)

        rvLogros.layoutManager = GridLayoutManager(requireContext(), 3)

        viewModel.cargarDatosUsuario(userId)
        viewModel.cargarLogrosUsuario(userId)

        viewModel.usuario.observe(viewLifecycleOwner) { user ->
            user?.let { setupUsuario(it, ivAvatar, tvNombre, tvNivel, pNivel) }
        }

        viewModel.logros.observe(viewLifecycleOwner) { listaLogros ->
            listaLogros?.let { setupLogros(it, rvLogros) }
        }
    }

    private fun setupUsuario(
        user: com.fiorella.plantguardian.data.schemas.UserProgressData,
        ivAvatar: ImageView,
        tvNombre: TextView,
        tvNivel: TextView,
        pNivel: ProgressBar
    ) {
        tvNombre.text = user.nombre
        tvNivel.text = "Nivel ${user.nivel}"

        animarProgressBar(pNivel, user.progreso_porcentaje)
        cargarAvatar(ivAvatar, user.icono)

        tvNombre.animate().alpha(1f).setStartDelay(100).setDuration(400).start()
        tvNivel.animate().alpha(1f).setStartDelay(150).setDuration(400).start()
        pNivel.animate().alpha(1f).setStartDelay(200).setDuration(400).start()
    }

    private fun setupLogros(listaLogros: List<AchievementData>, rvLogros: RecyclerView) {
        val adapter = rvLogros.adapter as? AchievementAdapter

        if (adapter == null) {
            rvLogros.adapter = AchievementAdapter(listaLogros)
            rvLogros.animate().alpha(1f).setDuration(400).start()
        } else {
            adapter.actualizarLista(listaLogros)
        }
    }

    private fun cargarAvatar(ivAvatar: ImageView, url: String) {
        ivAvatar.load(url) {
            crossfade(true)
            transformations(CircleCropTransformation())
            listener(onSuccess = { _, _ ->
                ivAvatar.animate().alpha(1f).setDuration(400).start()
            })
        }
    }

    private fun animarProgressBar(pNivel: ProgressBar, progreso: Int) {
        pNivel.progress = 0
        pNivel.animate()
            .setStartDelay(200)
            .setDuration(800)
            .withStartAction { pNivel.setProgress(progreso, true) }
            .start()
    }

    private fun ocultarVistas(vararg vistas: View) {
        vistas.forEach { it.alpha = 0f }
    }
}