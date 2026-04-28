package com.fiorella.plantguardian.ui.add_plant
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.GONE

        val imgRecibida = arguments?.getString("foto_uri")
        if (imgRecibida != null) {
            val uri = imgRecibida.toUri()
            val ivPlanta = view.findViewById<ImageView>(R.id.ivImagenCapturada)
            ivPlanta.setImageURI(uri)
        }

        view.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorPrincipal, AddPlantFragment())
                .commit()
        }

        view.findViewById<Button>(R.id.btnAtras).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnSiguientePaso2).setOnClickListener {
            // PASO 3
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //activity?.findViewById<View>(R.id.navMenu)?.visibility = View.VISIBLE
    }
}