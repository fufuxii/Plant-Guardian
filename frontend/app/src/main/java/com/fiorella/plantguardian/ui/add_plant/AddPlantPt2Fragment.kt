package com.fiorella.plantguardian.ui.add_plant
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R
import androidx.core.net.toUri

@Suppress("DEPRECATION")
class AddPlantPt2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt2, container, false)
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

        val foto_uri = arguments?.getString("foto_uri")
        val nombreComun = arguments?.getString("nombre_comun")
        val nombreCientifico = arguments?.getString("nombre_cientifico")

        if (foto_uri != null)
            view.findViewById<ImageView>(R.id.ivImagenCapturada).setImageURI(foto_uri.toUri())

        view.findViewById<TextView>(R.id.tvNombrePlanta).text = nombreComun
        view.findViewById<TextView>(R.id.tvNombreCientifico).text = nombreCientifico

        view.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener {
            activity?.findViewById<View>(R.id.navMenu)?.visibility = View.VISIBLE
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorPrincipal, AddPlantFragment())
                .commit()
        }

        view.findViewById<Button>(R.id.btnAtras).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnSiguientePaso2).setOnClickListener {
            //sig
        }
    }
}