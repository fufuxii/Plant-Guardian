package com.fiorella.plantguardian.ui.my_plants
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.PlantData
import com.fiorella.plantguardian.ui.adapters.PlantAdapter
import com.fiorella.plantguardian.ui.add_plant.AddPlantFragment
import com.fiorella.plantguardian.ui.extensions.navigateTo
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MyPlantsFragment : Fragment() {

    private val viewModel: ViewPlantModel by activityViewModels()
    private lateinit var adapter: PlantAdapter
    private var idUsuario: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_plants, container, false)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
        idUsuario = sharedPref.getString("user_id", null)
        val rv = view.findViewById<RecyclerView>(R.id.rvPlantas)
        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter = PlantAdapter(emptyList()) { planta ->
            abrirDetallePlanta(planta)
        }
        rv.adapter = adapter

        viewModel.plantas.observe(viewLifecycleOwner) { listaDePlantas ->
            adapter.actualizarLista(listaDePlantas)
        }

        viewModel.estaCargando.observe(viewLifecycleOwner) { cargando ->
            view.findViewById<View>(R.id.pbCargandoLista)?.visibility = if (cargando) View.VISIBLE else View.GONE
        }

        idUsuario?.let {
            viewModel.obtenerPlantas(it)
        } ?: Log.e("MyPlantsFragment", "Error: User ID is null")

        view.findViewById<FloatingActionButton>(R.id.fabAddPlant).setOnClickListener {
            val bottomNav = activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.navMenu)
            bottomNav?.selectedItemId = R.id.nav_add
            parentFragmentManager.navigateTo(AddPlantFragment(), R.id.contenedorPrincipal, addToBackStack = false)
        }
    }

    private fun abrirDetallePlanta(planta: PlantData) {
        val detalleFragment = ViewPlantTasksFragment()

        val bundle = Bundle()
        bundle.putSerializable("planta", planta)
        detalleFragment.arguments = bundle

        parentFragmentManager.navigateTo(detalleFragment, R.id.contenedorPrincipal)
    }
}