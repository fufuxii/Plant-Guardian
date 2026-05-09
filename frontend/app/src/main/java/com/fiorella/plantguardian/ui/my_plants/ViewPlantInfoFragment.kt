package com.fiorella.plantguardian.ui.my_plants
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.PlantData

@Suppress("DEPRECATION")
class ViewPlantInfoFragment : Fragment() {

    private var planta: PlantData? = null

    companion object {
        fun newInstance(planta: PlantData?): ViewPlantInfoFragment {
            val fragment = ViewPlantInfoFragment()
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
        return inflater.inflate(R.layout.fragment_view_plant_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNombreComun = view.findViewById<TextView>(R.id.tvNombreComun)
        val tvNombreCientifico = view.findViewById<TextView>(R.id.tvNombreCientifico)
        val tvTambienConocida = view.findViewById<TextView>(R.id.tvTambienConocida)
        val tvLugar = view.findViewById<TextView>(R.id.tvLugar)
        val tvDescripcion = view.findViewById<TextView>(R.id.tvDescripcionGeneral)

        planta?.let { p ->
            tvNombreComun.text = p.nombre_comun
            tvNombreCientifico.text = p.nombre_cientifico
            tvLugar.text = p.lugar
            tvDescripcion.text = p.descripcion_general ?: "-"

            tvTambienConocida.text = p.nombre_otros?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?.take(4)
                ?.joinToString(separator = "\n")
                ?: "-"
        }
    }
}