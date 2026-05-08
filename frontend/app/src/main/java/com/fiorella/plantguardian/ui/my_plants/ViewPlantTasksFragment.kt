package com.fiorella.plantguardian.ui.my_plants
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            planta = it.getSerializable("planta") as? PlantData
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_plant_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivFoto = view.findViewById<ImageView>(R.id.ivDetallePlanta)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombreDetalle)
        val rvHoy = view.findViewById<RecyclerView>(R.id.rvTareasActuales)
        val rvProximas = view.findViewById<RecyclerView>(R.id.rvTareasProximas)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val containerInfo = view.findViewById<View>(R.id.containerInfo)

        planta?.let { p ->
            tvNombre?.text = p.nombre_comun
            ivFoto?.load(p.imagen_url) {
                crossfade(true)
                crossfade(400)
                error(R.drawable.ic_plant)
            }
            cargarTareasDesdeBackend(p.id_usuario_planta, rvHoy, rvProximas, containerInfo)
        }

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.GONE
    }
    private fun cargarTareasDesdeBackend(
        idPlanta: String,
        rvHoy: RecyclerView,
        rvFuturo: RecyclerView,
        containerInfo: View?
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
                }
            } catch (e: Exception) {
                Log.e("ViewPlantTasks", "Error: ${e.message}")
            } finally {
                containerInfo?.animate()
                    ?.alpha(1f)
                    ?.setDuration(400)
                    ?.start()
            }
        }
    }
}