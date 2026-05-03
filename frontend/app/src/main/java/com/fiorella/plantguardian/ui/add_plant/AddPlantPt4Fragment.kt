package com.fiorella.plantguardian.ui.add_plant
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R

class AddPlantPt4Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt4, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tempId = arguments?.getString("temp_id")
        val lugar = arguments?.getString("lugar_planta")
        val estado = arguments?.getString("estado_salud") ?: "Saludable"
        val problema = arguments?.getString("problema_detectado") ?: "Ninguno"
        val descripcion = arguments?.getString("descripcion_diagnostico") ?: "Tu planta se encuentra en óptimas condiciones."
        val consejos = arguments?.getString("consejos_cuidado") ?: "Sigue con el riego habitual."

        view.findViewById<TextView>(R.id.tvValorEstado).text = estado
        view.findViewById<TextView>(R.id.tvValorProblema).text = problema
        view.findViewById<TextView>(R.id.tvValorDescripcion).text = descripcion
        view.findViewById<TextView>(R.id.tvValorConsejos).text = consejos

        view.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener {
            activity?.findViewById<View>(R.id.navMenu)?.visibility = View.VISIBLE
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorPrincipal, AddPlantFragment())
                .commit()
        }

        view.findViewById<Button>(R.id.btnAtras).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnSiguientePaso4).setOnClickListener {
            guardarPlantaFinal(tempId, lugar)
        }
    }

    private fun guardarPlantaFinal(tempId: String?, lugar: String?) {
    }
}