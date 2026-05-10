package com.fiorella.plantguardian.ui.add_plant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.TaskResponse
import com.fiorella.plantguardian.data.schemas.UserRequest
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.ui.extensions.navigateClose
import com.fiorella.plantguardian.ui.main.MainActivity
import com.fiorella.plantguardian.ui.my_plants.MyPlantsFragment
import com.fiorella.plantguardian.ui.tools.models.AddPlantViewModel
import com.fiorella.plantguardian.ui.tools.models.MyPlantsViewModel
import kotlinx.coroutines.launch
import java.util.ArrayList

class AddPlantPt5Fragment : Fragment() {

    private var layoutCargando: View? = null

    private val viewModel: MyPlantsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt5, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutCargando = view.findViewById(R.id.layoutCargando)
        configurarListaTareas(view)
        configurarBotones(view)
    }

    private fun configurarListaTareas(view: View) {
        val contenedor = view.findViewById<LinearLayout>(R.id.llContenedorTareas)

        val listaRaw = arguments?.let { bundle ->
            BundleCompat.getSerializable(bundle, "tareas", ArrayList::class.java)
        } ?: arrayListOf<Any>()

        val listaTareas = ArrayList(listaRaw.filterIsInstance<TaskResponse>())

        listaTareas.forEach { tarea ->
            val itemView = layoutInflater.inflate(R.layout.item_list_tasks, contenedor, false)
            itemView.findViewById<TextView>(R.id.tvNombreTarea).text = tarea.tarea
            itemView.findViewById<TextView>(R.id.tvFrecuenciaTarea).text = tarea.frecuencia
            contenedor.addView(itemView)
        }
    }

    private fun configurarBotones(view: View) {
        view.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener {
            cerrarFlujo()
        }

        view.findViewById<Button>(R.id.btnAtras).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnFinalizar).setOnClickListener {
            guardarPlanta()
        }
    }

    private fun guardarPlanta() {
        val tempId = arguments?.getString("temp_id") ?: ""
        val idUsuario = arguments?.getString("id_usuario") ?: ""

        layoutCargando?.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.guardarPlanta(
                    tempId,
                    UserRequest(idUsuario)
                )
                if (response.isSuccessful) {
                    gestionarGuardado(idUsuario)
                } else {
                    mostrarError("Error al guardar la planta")
                }
            } catch (e: Exception) {
                Log.e("DEBUG_PLANT", "Error final: ${e.message}")
                mostrarError("Error de conexión al guardar")
            } finally {
                layoutCargando?.visibility = View.GONE
            }
        }
    }

    private fun gestionarGuardado(idUsuario: String) {
        val addViewModel: AddPlantViewModel by activityViewModels()
        addViewModel.resultadoAnalisis.value = null

        viewModel.obtenerPlantas(idUsuario, forzarRecarga = true)
        parentFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)

        (activity as? MainActivity)?.let { main ->
            main.mostrarNav()
            main.asignarSeleccionMenu(R.id.nav_plants)
        }

        Toast.makeText(requireContext(), "¡Planta guardada!", Toast.LENGTH_SHORT).show()
    }

    private fun mostrarError(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun cerrarFlujo() {
        val addPlantViewModel: AddPlantViewModel by activityViewModels()
        addPlantViewModel.resultadoAnalisis.value = null
        addPlantViewModel.ultimoLugarAnalizado = null
        addPlantViewModel.ultimoTempIdAnalizado = null

        (activity as? MainActivity)?.mostrarNav()
        parentFragmentManager.navigateClose(AddPlantFragment(), R.id.contenedorPrincipal)
    }
}