package com.fiorella.plantguardian.ui.my_plants

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.PlantData
import com.fiorella.plantguardian.ui.tools.adapters.PlantAdapter
import com.fiorella.plantguardian.ui.add_plant.AddPlantFragment
import com.fiorella.plantguardian.ui.main.MainActivity
import com.fiorella.plantguardian.ui.tools.models.MyPlantsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MyPlantsFragment : Fragment() {

    private val viewModel: MyPlantsViewModel by activityViewModels()
    private lateinit var adapter: PlantAdapter
    private var idUsuario: String? = null
    private var listaCompleta: List<PlantData> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_plants, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cargarUsuario()
        configurarRecyclerView(view)
        configurarBusqueda(view)
        observarViewModel()
        configurarFab(view)
    }

    private fun cargarUsuario() {
        val sharedPref = requireActivity()
            .getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
        idUsuario = sharedPref.getString("user_id", null)
    }

    private fun configurarRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rvPlantas)
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = PlantAdapter(emptyList()) { planta -> abrirDetallePlanta(planta) }
        rv.adapter = adapter
    }

    private fun configurarBusqueda(view: View) {
        view.findViewById<EditText>(R.id.etBuscarPlanta).addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    filtrarPlantas(s.toString())
                }
            }
        )
    }

    private fun configurarFab(view: View) {
        view.findViewById<FloatingActionButton>(R.id.fabAddPlant).setOnClickListener {
            (activity as? MainActivity)?.asignarSeleccionMenu(R.id.nav_add)
        }
    }

    private fun observarViewModel() {
        (activity as? MainActivity)?.mostrarNav()

        viewModel.plantas.observe(viewLifecycleOwner) { lista ->
            if (lista != null) {
                listaCompleta = lista
                adapter.actualizarLista(lista)
            }
        }

        idUsuario?.let {
            viewModel.obtenerPlantas(it)
        } ?: Log.e("PlantsFragment", "Error: User ID is null")
    }

    private fun filtrarPlantas(query: String) {
        val filtradas = if (query.isEmpty()) {
            listaCompleta
        } else {
            listaCompleta.filter { planta ->
                planta.nombre_comun.contains(query, ignoreCase = true)
            }
        }
        adapter.actualizarLista(filtradas)
    }

    private fun abrirDetallePlanta(planta: PlantData) {
        val detalleFragment = ViewPlantFragment().apply {
            arguments = Bundle().apply {
                putSerializable("planta", planta)
            }
        }
        (activity as? MainActivity)?.cargarFragmento(detalleFragment, "DetallePlanta", true)
    }
}