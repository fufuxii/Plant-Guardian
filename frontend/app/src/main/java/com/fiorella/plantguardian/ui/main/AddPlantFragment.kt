package com.fiorella.plantguardian.ui.main
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.ui.anadir_planta.AddPlantPt1Fragment

class AddPlantFragment : Fragment () {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_plant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnAdd = view.findViewById<CardView>(R.id.btnAnadirPlanta)
        btnAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorPrincipal, AddPlantPt1Fragment())
                .addToBackStack(null)
                .commit()
        }
    }
}