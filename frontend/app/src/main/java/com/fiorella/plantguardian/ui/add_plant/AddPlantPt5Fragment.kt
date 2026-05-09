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
import androidx.lifecycle.lifecycleScope
import com.fiorella.plantguardian.R
import kotlin.collections.arrayListOf
import kotlin.collections.forEach
import com.fiorella.plantguardian.data.model.TaskResponse
import com.fiorella.plantguardian.data.model.UserRequest
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.ui.extensions.navigateClose
import kotlinx.coroutines.launch

class AddPlantPt5Fragment : Fragment() {
    private var layoutCargando: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt5, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutCargando = view.findViewById(R.id.layoutCargando)
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

        view.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener {
            parentFragmentManager.navigateClose(AddPlantFragment(), R.id.contenedorPrincipal)
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

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.guardarPlanta(
                    tempId,
                    UserRequest(idUsuario)
                )
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Planta añadida con éxito.", Toast.LENGTH_LONG).show()
                    parentFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                } else {
                    Toast.makeText(requireContext(), "Error al guardar la planta", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("DEBUG_PLANT", "Error final: ${e.message}")
                Toast.makeText(requireContext(), "Error de conexión al guardar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}