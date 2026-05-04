package com.fiorella.plantguardian.ui.add_plant
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.network.RetrofitClient
import kotlinx.coroutines.launch

class AddPlantPt4Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt4, container, false)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tempId = arguments?.getString("temp_id") ?: ""
        val lugar = arguments?.getString("lugar") ?: ""
        val idUsuario = arguments?.getString("id_usuario") ?: ""

        val loadingView = view.findViewById<View>(R.id.layoutCargando)
        val contentView = view.findViewById<View>(R.id.contenedorInfo)

        loadingView.visibility = View.VISIBLE
        contentView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.analizarPlanta(tempId, lugar, idUsuario)

                if (response.isSuccessful && response.body() != null) {
                    val analisis = response.body()!!

                    view.findViewById<TextView>(R.id.tvValorEstado).text = analisis.estado
                    view.findViewById<TextView>(R.id.tvValorProblema).text = analisis.problema
                    view.findViewById<TextView>(R.id.tvValorDescripcion).text = analisis.descripcion

                    val consejosFormateados = analisis.consejos.joinToString(separator = "\n\n") { "• $it" }
                    view.findViewById<TextView>(R.id.tvValorConsejos).text = consejosFormateados

                    loadingView.visibility = View.GONE
                    contentView.visibility = View.VISIBLE
                } else {
                    finalizarConError("No se ha podido procesar el análisis")
                }
            } catch (e: Exception) {
                Log.e("DEBUG_PLANT", "Fallo análisis: ${e.localizedMessage}")
                finalizarConError("No se ha podido procesar el análisis")
            }
        }

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
            guardarPlanta(tempId, lugar)
        }
    }

    private fun finalizarConError(mensaje: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    private fun guardarPlanta(tempId: String?, lugar: String?) {
        // pt5
    }
}