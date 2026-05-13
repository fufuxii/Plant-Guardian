package com.fiorella.plantguardian.ui.my_plants.sections

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.PlantData
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.data.schemas.TaskData
import com.fiorella.plantguardian.ui.tools.adapters.TaskAdapter
import com.fiorella.plantguardian.ui.tools.models.UserViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class ViewPlantTasksFragment : Fragment() {

    private var planta: PlantData? = null
    private val userViewModel: UserViewModel by activityViewModels()

    companion object {
        fun newInstance(planta: PlantData?): ViewPlantTasksFragment {
            return ViewPlantTasksFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("planta", planta)
                }
            }
        }
    }

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
        return inflater.inflate(R.layout.fragment_view_plant_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepararElementos(view)
        planta?.let { p ->
            obtenerTareas(view, p.id_usuario_planta)
        }
    }

    private fun prepararElementos(view: View) {
        val vistasAAnimar = listOf(
            R.id.rvTareasActuales, R.id.rvTareasProximas,
            R.id.tvTareasActuales, R.id.tvTareasProximas,
            R.id.tvAvisoSinTareasHoy, R.id.tvAvisoSinTareasProximas
        )

        vistasAAnimar.forEach { id ->
            view.findViewById<View>(id)?.apply {
                alpha = 0f
                translationY = 30f
            }
        }
    }

    private fun obtenerTareas(view: View, idPlanta: String) {
        val rvHoy = view.findViewById<RecyclerView>(R.id.rvTareasActuales)
        val rvProximas = view.findViewById<RecyclerView>(R.id.rvTareasProximas)
        val tvTituloHoy = view.findViewById<TextView>(R.id.tvTareasActuales)
        val tvTituloProximas = view.findViewById<TextView>(R.id.tvTareasProximas)
        val tvAvisoHoy = view.findViewById<TextView>(R.id.tvAvisoSinTareasHoy)
        val tvAvisoProximas = view.findViewById<TextView>(R.id.tvAvisoSinTareasProximas)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.obtenerTareasPlanta(idPlanta)

                if (response.isSuccessful && response.body() != null) {
                    val hoyIso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val tareasList = response.body()!!

                    val tareasLimpias = tareasList.map {
                        it.copy(fecha_proxima = it.fecha_proxima.substringBefore("T"))
                    }

                    val listaHoy = tareasLimpias.filter { it.fecha_proxima <= hoyIso }
                    val listaProximas = tareasLimpias.filter { it.fecha_proxima > hoyIso }

                    configurarSeccion(listaHoy, rvHoy, tvAvisoHoy, esHoy = true)
                    configurarSeccion(listaProximas, rvProximas, tvAvisoProximas, esHoy = false)

                    ejecutarAnimaciones(tvTituloHoy, rvHoy, tvAvisoHoy, tvTituloProximas, rvProximas, tvAvisoProximas, listaHoy.isEmpty(), listaProximas.isEmpty())
                }
            } catch (e: Exception) {
                Log.e("ViewPlantTasks", "Error al cargar tareas: ${e.message}")
            }
        }
    }

    private fun configurarSeccion(lista: List<TaskData>, rv: RecyclerView, aviso: TextView?, esHoy: Boolean) {
        if (lista.isEmpty()) {
            aviso?.visibility = View.VISIBLE
            rv.visibility = View.GONE
        } else {
            aviso?.visibility = View.GONE
            rv.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(requireContext())
                adapter = TaskAdapter(lista, esHoy = esHoy) { tarea ->
                    mostrarDialogoConfirmacionTarea(tarea)
                }
            }
        }
    }

    private fun ejecutarCompletarTarea(tarea: TaskData) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.completarTarea(tarea.id)

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "¡Tarea guardada!", Toast.LENGTH_SHORT).show()

                    val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
                    val userId = prefs.getString("user_id", "") ?: ""
                    userViewModel.cargarDatosUsuario(userId, forzar = true)

                    planta?.let { obtenerTareas(requireView(), it.id_usuario_planta) }
                }
            } catch (e: Exception) {
                Log.e("ViewPlantTasks", "Error al completar: ${e.message}")
            }
        }
    }

    private fun mostrarDialogoConfirmacionTarea(tarea: TaskData) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_confirm_task, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.findViewById<MaterialButton>(R.id.btnCancelar).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<MaterialButton>(R.id.btnAceptar).setOnClickListener {
            dialog.dismiss()
            ejecutarCompletarTarea(tarea)
        }

        dialog.show()

        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.88).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun ejecutarAnimaciones(
        t1: View, r1: View, a1: View?,
        t2: View, r2: View, a2: View?,
        vacioHoy: Boolean, vacioProx: Boolean
    ) {
        configurarAnimacion(t1, 80)
        configurarAnimacion(if (vacioHoy) a1!! else r1, 160)

        configurarAnimacion(t2, 260)
        configurarAnimacion(if (vacioProx) a2!! else r2, 340)
    }

    private fun configurarAnimacion(view: View, delay: Long) {
        view.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(delay)
            .setDuration(350)
            .start()
    }
}