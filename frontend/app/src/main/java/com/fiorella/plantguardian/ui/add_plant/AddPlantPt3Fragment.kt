package com.fiorella.plantguardian.ui.add_plant
import com.fiorella.plantguardian.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
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

        val lugarAnterior = arguments?.getString("lugar")
        if (!lugarAnterior.isNullOrEmpty()) {
            autoCompleteTextView.setText(lugarAnterior, false)
        }

        autoCompleteTextView.setOnClickListener {
            val textoActual = autoCompleteTextView.text.toString()
            autoCompleteTextView.setText(textoActual, false)
            autoCompleteTextView.showDropDown()
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

        view.findViewById<Button>(R.id.btnSiguientePaso3).setOnClickListener {
            val lugarSeleccionado = autoCompleteTextView.text.toString()

            if (lugarSeleccionado.isNotEmpty()) {
                val fotoUri = arguments?.getString("foto_uri")
                val nombreComun = arguments?.getString("nombre_comun")
                val nombreCientifico = arguments?.getString("nombre_cientifico")
                val tempId = arguments?.getString("temp_id")

                val bundle = Bundle().apply {
                    putString("foto_uri", fotoUri)
                    putString("nombre_comun", nombreComun)
                    putString("nombre_cientifico", nombreCientifico)
                    putString("temp_id", tempId)
                    putString("lugar", lugarSeleccionado)
                }

                val paso4 = AddPlantPt4Fragment()
                paso4.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.contenedorPrincipal, paso4)
                    .addToBackStack(null)
                    .commit()

            } else {
                Toast.makeText(requireContext(), "Por favor, indica dónde está la planta.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class NoFilterAdapter(context: android.content.Context, layout: Int, val items: Array<String>) :
        ArrayAdapter<String>(context, layout, items) {

        override fun getFilter(): android.widget.Filter {
            return object : android.widget.Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val results = FilterResults()
                    results.values = items
                    results.count = items.size
                    return results
                }
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    notifyDataSetChanged()
                }
            }
        }
    }
}