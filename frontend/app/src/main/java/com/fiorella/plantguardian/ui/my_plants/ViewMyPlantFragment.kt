package com.fiorella.plantguardian.ui.my_plants
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.PlantData
import com.fiorella.plantguardian.ui.adapters.ViewPlantAdapter
import com.fiorella.plantguardian.ui.main.MainActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@Suppress("DEPRECATION")
class ViewMyPlantFragment : Fragment() {
    private var planta: PlantData? = null

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
        return inflater.inflate(R.layout.fragment_view_plant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.ocultarNav()

        val ivFoto = view.findViewById<ImageView>(R.id.ivDetallePlanta)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombreDetalle)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayoutDetalle)
        val viewPager = view.findViewById<ViewPager2>(R.id.vpDetalle)

        planta?.let { p ->
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

        btnBack.setOnClickListener {
            (activity as? MainActivity)?.mostrarNav()
            parentFragmentManager.popBackStack()
        }

        btnDelete.setOnClickListener {
            // borrar planta
        }
    }
}