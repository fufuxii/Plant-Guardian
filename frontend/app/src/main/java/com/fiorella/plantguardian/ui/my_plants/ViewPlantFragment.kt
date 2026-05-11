package com.fiorella.plantguardian.ui.my_plants

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.PlantData
import com.fiorella.plantguardian.ui.tools.adapters.ViewPlantAdapter
import com.fiorella.plantguardian.ui.main.MainActivity
import com.fiorella.plantguardian.ui.tools.models.MyPlantsViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@Suppress("DEPRECATION")
class ViewPlantFragment : Fragment() {

    private var planta: PlantData? = null
    private val myPlantsModel: MyPlantsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            planta = it.getSerializable("planta") as? PlantData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_plant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.ocultarNav()

        planta?.let { p ->
            configurarEncabezado(view, p)
        }

        configurarViewPager(view)
        configurarBotones(view)
    }

    private fun configurarEncabezado(view: View, p: PlantData) {
        val ivFoto = view.findViewById<ImageView>(R.id.ivDetallePlanta)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombreDetalle)

        tvNombre.text = p.nombre_comun

        ivFoto.alpha = 0f
        ivFoto.load(p.imagen_url) {
            crossfade(false)
            error(R.drawable.ic_plant)
            listener(
                onSuccess = { _, _ ->
                    ivFoto.animate().alpha(1f).setDuration(400).start()
                },
                onError = { _, _ ->
                    ivFoto.animate().alpha(1f).setDuration(400).start()
                }
            )
        }
    }

    private fun configurarViewPager(view: View) {
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayoutDetalle)
        val viewPager = view.findViewById<ViewPager2>(R.id.vpDetalle)

        val adapter = ViewPlantAdapter(this, planta)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tareas)
                1 -> getString(R.string.diagn_stico)
                2 -> getString(R.string.informaci_n)
                else -> ""
            }
        }.attach()
    }

    private fun configurarBotones(view: View) {
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnDelete.setOnClickListener {
            planta?.id_usuario_planta?.let { id ->
                mostrarDialogoConfirmacion(id)
            }
        }
    }

    private fun mostrarDialogoConfirmacion(id: String) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_confirm_delete, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.findViewById<MaterialButton>(R.id.btnCancelar).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<MaterialButton>(R.id.btnAceptar).setOnClickListener {
            ejecutarBorradoPlanta(id)
            dialog.dismiss()
        }

        dialog.show()

        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.88).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun ejecutarBorradoPlanta(idPlanta: String) {
        val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
        val idUsuario = prefs.getString("user_id", "") ?: ""

        myPlantsModel.eliminarPlanta(idPlanta, idUsuario) { exito ->
            if (exito) {
                Toast.makeText(context, "Planta eliminada con éxito", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "Error al eliminar la planta", Toast.LENGTH_SHORT).show()
            }
        }
    }
}