package com.fiorella.plantguardian.ui.my_plants
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.PlantData
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.ui.adapters.TaskAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class ViewPlantTasksFragment : Fragment() {

    private var planta: PlantData? = null

    companion object {
        fun newInstance(planta: PlantData?): ViewPlantTasksFragment {
            val fragment = ViewPlantTasksFragment()
            val args = Bundle()
            args.putSerializable("planta", planta)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            planta = it.getSerializable("planta") as? PlantData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_plant_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvHoy = view.findViewById<RecyclerView>(R.id.rvTareasActuales)
        val rvProximas = view.findViewById<RecyclerView>(R.id.rvTareasProximas)
        val tvHoy = view.findViewById<TextView>(R.id.tvTareasActuales)
        val tvProximas = view.findViewById<TextView>(R.id.tvTareasProximas)

        rvHoy.alpha = 0f
        rvHoy.translationY = 30f
        rvProximas.alpha = 0f
        rvProximas.translationY = 30f
        tvHoy.alpha = 0f
        tvHoy.translationY = 30f
        tvProximas.alpha = 0f
        tvProximas.translationY = 30f

        planta?.let {
            cargarTareasDesdeBackend(it.id_usuario_planta, rvHoy, rvProximas, tvHoy, tvProximas)
        }
    }

    private fun cargarTareasDesdeBackend(
        idPlanta: String,
        rvHoy: RecyclerView,
        rvFuturo: RecyclerView,
        tvHoy: TextView,
        tvProximas: TextView
    ) {
        val sdfIso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val hoyIso = sdfIso.format(Date())
        val tvAvisoHoy = view?.findViewById<TextView>(R.id.tvAvisoSinTareasHoy)
        val tvAvisoProximas = view?.findViewById<TextView>(R.id.tvAvisoSinTareasProximas)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.obtenerTareasPlanta(idPlanta)

                if (response.isSuccessful && response.body() != null) {
                    val tareasList = response.body()!!
                    val tareasLimpias = tareasList.map { tarea ->
                        tarea.copy(fecha_proxima = tarea.fecha_proxima.substringBefore("T"))
                    }
                    val tareasHoy = tareasLimpias.filter { it.fecha_proxima <= hoyIso }
                    val tareasProximas = tareasLimpias.filter { it.fecha_proxima > hoyIso }

                    if (tareasHoy.isEmpty()) {
                        tvAvisoHoy?.visibility = View.VISIBLE
                        rvHoy.visibility = View.GONE
                    } else {
                        tvAvisoHoy?.visibility = View.GONE
                        rvHoy.visibility = View.VISIBLE
                        rvHoy.layoutManager = LinearLayoutManager(requireContext())
                        rvHoy.adapter = TaskAdapter(tareasHoy, esHoy = true)
                    }

                    if (tareasProximas.isEmpty()) {
                        tvAvisoProximas?.visibility = View.VISIBLE
                        rvFuturo.visibility = View.GONE
                    } else {
                        tvAvisoProximas?.visibility = View.GONE
                        rvFuturo.visibility = View.VISIBLE
                        rvFuturo.layoutManager = LinearLayoutManager(requireContext())
                        rvFuturo.adapter = TaskAdapter(tareasProximas, esHoy = false)
                    }

                    tvHoy.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setStartDelay(80)
                        .setDuration(300)
                        .start()

                    rvHoy.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setStartDelay(160)
                        .setDuration(350)
                        .start()

                    tvProximas.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setStartDelay(260)
                        .setDuration(300)
                        .start()

                    rvFuturo.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setStartDelay(340)
                        .setDuration(350)
                        .start()
                }
            } catch (e: Exception) {
                Log.e("ViewPlantTasks", "Error: ${e.message}")
            }
        }
    }
}