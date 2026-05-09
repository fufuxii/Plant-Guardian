package com.fiorella.plantguardian.ui.my_plants
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.PlantData

@Suppress("DEPRECATION")
class ViewPlantDiagnosisFragment : Fragment() {

    private var planta: PlantData? = null

    companion object {
        fun newInstance(planta: PlantData?): ViewPlantDiagnosisFragment {
            val fragment = ViewPlantDiagnosisFragment()
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
        return inflater.inflate(R.layout.fragment_view_plant_diagnosis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvEstado = view.findViewById<TextView>(R.id.tvValorEstado)
        val tvProblema = view.findViewById<TextView>(R.id.tvValorProblema)
        val tvDescripcion = view.findViewById<TextView>(R.id.tvValorDescripcion)
        val tvConsejos = view.findViewById<TextView>(R.id.tvValorConsejos)
        val btnVolverAnalizar = view.findViewById<AppCompatButton>(R.id.btnVolverAnalizar)

        planta?.let { p ->
            tvEstado.text = p.estado ?: "-"
            tvProblema.text = p.problema ?: "-"
            tvDescripcion.text = p.descripcion_usuario ?: "-"
            tvConsejos.text = p.consejos
                ?.joinToString(separator = "\n\n") { "• $it" }
                ?: "-"
        }

        btnVolverAnalizar.setOnClickListener {
            // volver a analizar
        }
    }
}