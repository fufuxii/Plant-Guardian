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
                val prefs = requireContext().getSharedPreferences("PlantGuardianPrefs", android.content.Context.MODE_PRIVATE)
                val userId = prefs.getString("user_id", "")

                val bundle = Bundle().apply {
                    putString("foto_uri", arguments?.getString("foto_uri"))
                    putString("nombre_comun", arguments?.getString("nombre_comun"))
                    putString("nombre_cientifico", arguments?.getString("nombre_cientifico"))
                    putString("temp_id", arguments?.getString("temp_id"))
                    putString("lugar", lugarSeleccionado)
                    putString("id_usuario", userId)
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