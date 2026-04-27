package com.fiorella.plantguardian.ui.anadir_planta
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.ui.main.AddPlantFragment

class AddPlantPt1Fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.GONE

        view.findViewById<ImageView>(R.id.btnCerrar).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorPrincipal, AddPlantFragment())
                .commit()
        }

        view.findViewById<Button>(R.id.btnSiguientePaso1).setOnClickListener {
            // Paso 2
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.VISIBLE
    }
}