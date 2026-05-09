package com.fiorella.plantguardian.ui.my_plants.sections
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.PlantData

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
        planta?.let { p ->
            configurarElementos(view, p)
            configurarTexto(view, p.nombre_otros)
        }
    }

    private fun configurarElementos(view: View, p: PlantData) {
        view.findViewById<TextView>(R.id.tvNombreComun).text = p.nombre_comun
        view.findViewById<TextView>(R.id.tvNombreCientifico).text = p.nombre_cientifico
        view.findViewById<TextView>(R.id.tvLugar).text = p.lugar
        view.findViewById<TextView>(R.id.tvDescripcionGeneral).text = p.descripcion_general ?: "-"
    }

    private fun configurarTexto(view: View, nombresOtros: String?) {
        val tvTambienConocida = view.findViewById<TextView>(R.id.tvTambienConocida)
        tvTambienConocida.text = nombresOtros?.split(",")
            ?.asSequence()
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.take(4)?.joinToString(separator = "\n") { "• $it" }
            ?.ifEmpty { "-" } ?: "-"
    }
}