package com.fiorella.plantguardian.ui.add_plant
import com.fiorella.plantguardian.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class AddPlantPt3Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt3, container, false)
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

        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.actvPlantaLugar)
        val opciones = resources.getStringArray(R.array.plantas_lugares)
        val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_plant, opciones)

        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }

        view.findViewById<Button>(R.id.btnSiguientePaso3).setOnClickListener {
            val lugarSeleccionado = autoCompleteTextView.text.toString()
            if (lugarSeleccionado.isNotEmpty()) {
                //avanzarAlUltimoPaso(lugarSeleccionado)
            } else {
                Toast.makeText(requireContext(), "Por favor, indica dónde está la planta", Toast.LENGTH_SHORT).show()
            }
        }
    }
}