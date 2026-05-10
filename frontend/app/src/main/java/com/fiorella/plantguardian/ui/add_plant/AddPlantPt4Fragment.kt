package com.fiorella.plantguardian.ui.add_plant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.TaskResponse
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.data.schemas.AnalisisResponse
import com.fiorella.plantguardian.ui.extensions.navigateClose
import com.fiorella.plantguardian.ui.extensions.navigateTo
import com.fiorella.plantguardian.ui.main.MainActivity
import com.fiorella.plantguardian.ui.tools.models.AddPlantViewModel
import kotlinx.coroutines.launch

class AddPlantPt4Fragment : Fragment() {

    private var tareasObtenidas: List<TaskResponse>? = null

    private val addPlantViewModel: AddPlantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tempId = arguments?.getString("temp_id") ?: ""
        val lugar = arguments?.getString("lugar") ?: ""
        val idUsuario = arguments?.getString("id_usuario") ?: ""

        configurarBotones(view, tempId, lugar, idUsuario)

        val esMismoLugar = addPlantViewModel.ultimoLugarAnalizado == lugar
        val esMismaImagen = addPlantViewModel.ultimoTempIdAnalizado == tempId
        val tieneResultado = addPlantViewModel.resultadoAnalisis.value != null

        if (tieneResultado && esMismoLugar && esMismaImagen) {
            mostrarResultadoAnalisis(view, addPlantViewModel.resultadoAnalisis.value!!)
            view.findViewById<View>(R.id.layoutCargando).visibility = View.GONE
            view.findViewById<View>(R.id.contenedorInfo).visibility = View.VISIBLE
        } else {
            ejecutarAnalisis(view, tempId, lugar, idUsuario)
        }
    }

    private fun ejecutarAnalisis(view: View, tempId: String, lugar: String, idUsuario: String) {
        val loadingView = view.findViewById<View>(R.id.layoutCargando)
        val contentView = view.findViewById<View>(R.id.contenedorInfo)

        loadingView.visibility = View.VISIBLE
        contentView.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.analizarPlanta(tempId, lugar, idUsuario)

                if (response.isSuccessful && response.body() != null) {
                    val analisis = response.body()!!

                    addPlantViewModel.resultadoAnalisis.value = analisis
                    addPlantViewModel.ultimoLugarAnalizado = lugar
                    addPlantViewModel.ultimoTempIdAnalizado = tempId

                    mostrarResultadoAnalisis(view, response.body()!!)

                    loadingView.visibility = View.GONE
                    contentView.visibility = View.VISIBLE
                } else {
                    finalizarConError("No se ha podido procesar el análisis")
                }
            } catch (e: Exception) {
                Log.e("DEBUG_PLANT", "Fallo análisis: ${e.localizedMessage}")
                finalizarConError("No se ha podido procesar el análisis")
            }
        }
    }

    private fun mostrarResultadoAnalisis(view: View, analisis: AnalisisResponse) {
        tareasObtenidas = analisis.tareas

        view.findViewById<TextView>(R.id.tvValorEstado).text = analisis.estado
        view.findViewById<TextView>(R.id.tvValorProblema).text = analisis.problema
        view.findViewById<TextView>(R.id.tvValorDescripcion).text = analisis.descripcion

        val consejosFormateados = analisis.consejos.joinToString(separator = "\n\n") { "• $it" }
        view.findViewById<TextView>(R.id.tvValorConsejos).text = consejosFormateados
    }

    private fun configurarBotones(view: View, tempId: String, lugar: String, idUsuario: String) {
        view.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener {
            cerrarFlujo()
        }

        view.findViewById<Button>(R.id.btnAtras).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnSiguientePaso4).setOnClickListener {
            redireccionarSiguientePaso(tempId, lugar, idUsuario)
        }
    }

    private fun redireccionarSiguientePaso(tempId: String, lugar: String, idUsuario: String) {
        if (tareasObtenidas == null) {
            Toast.makeText(requireContext(), "Espera a que termine el análisis", Toast.LENGTH_SHORT).show()
            return
        }

        val bundle = Bundle().apply {
            putString("temp_id", tempId)
            putString("lugar", lugar)
            putString("id_usuario", idUsuario)
            putSerializable("tareas", ArrayList(tareasObtenidas!!))
        }

        val paso5 = AddPlantPt5Fragment().apply { arguments = bundle }
        parentFragmentManager.navigateTo(paso5, R.id.contenedorPrincipal)
    }

    @Suppress("SameParameterValue")
    private fun finalizarConError(mensaje: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    private fun cerrarFlujo() {
        val addViewModel: AddPlantViewModel by activityViewModels()
        addViewModel.resultadoAnalisis.value = null

        (activity as? MainActivity)?.mostrarNav()
        parentFragmentManager.navigateClose(AddPlantFragment(), R.id.contenedorPrincipal)
    }
}