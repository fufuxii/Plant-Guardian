package com.fiorella.plantguardian.ui.add_plant

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.ui.tools.adapters.NoFilterAdapter
import com.fiorella.plantguardian.ui.extensions.navigateClose
import com.fiorella.plantguardian.ui.extensions.navigateTo
import com.fiorella.plantguardian.ui.main.MainActivity

class AddPlantPt3Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actvLugar = configurarDropdown(view)
        configurarBotones(view, actvLugar)
    }

    private fun configurarDropdown(view: View): AutoCompleteTextView {
        val actvLugar = view.findViewById<AutoCompleteTextView>(R.id.actvPlantaLugar)
        val opciones = resources.getStringArray(R.array.plantas_lugares)
        val adapter = NoFilterAdapter(requireContext(), R.layout.item_dropdown_plant, opciones)

        actvLugar.apply {
            setAdapter(adapter)
            setText("", false)
            setOnClickListener { showDropDown() }
        }
        return actvLugar
    }

    private fun configurarBotones(view: View, actvLugar: AutoCompleteTextView) {
        view.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener {
            cerrarFlujo()
        }

        view.findViewById<Button>(R.id.btnAtras).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnSiguientePaso3).setOnClickListener {
            validarLugar(actvLugar.text.toString())
        }
    }

    private fun validarLugar(lugarSeleccionado: String) {
        if (lugarSeleccionado.isNotEmpty()) {
            redireccionarSiguientePaso(lugarSeleccionado)
        } else {
            Toast.makeText(requireContext(), "Por favor, indica dónde está la planta.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun redireccionarSiguientePaso(lugarSeleccionado: String) {
        val userId = obtenerUserId()

        val bundle = Bundle().apply {
            putString("foto_uri", arguments?.getString("foto_uri"))
            putString("nombre_comun", arguments?.getString("nombre_comun"))
            putString("nombre_cientifico", arguments?.getString("nombre_cientifico"))
            putString("temp_id", arguments?.getString("temp_id"))
            putString("lugar", lugarSeleccionado)
            putString("id_usuario", userId)
        }

        val paso4 = AddPlantPt4Fragment().apply {
            arguments = bundle
        }

        parentFragmentManager.navigateTo(paso4, R.id.contenedorPrincipal)
    }

    private fun obtenerUserId(): String? {
        val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", Context.MODE_PRIVATE)
        return prefs.getString("user_id", "")
    }

    private fun cerrarFlujo() {
        (activity as? MainActivity)?.mostrarNav()
        parentFragmentManager.navigateClose(AddPlantFragment(), R.id.contenedorPrincipal)
    }
}