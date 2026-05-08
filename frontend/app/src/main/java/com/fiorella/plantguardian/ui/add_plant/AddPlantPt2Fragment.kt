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
import coil.load
import com.fiorella.plantguardian.ui.extensions.navigateClose
import com.fiorella.plantguardian.ui.extensions.navigateTo

@Suppress("DEPRECATION")
class AddPlantPt2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt2, container, false)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fotoUri1 = arguments?.getString("foto_uri")
        val nombreComun = arguments?.getString("nombre_comun")
        val nombreCientifico = arguments?.getString("nombre_cientifico")

        if (fotoUri1 != null) {
            val ivFoto = view.findViewById<ImageView>(R.id.ivImagenCapturada)
            ivFoto.alpha = 0f
            ivFoto.load(fotoUri1.toUri()) {
                listener(
                    onSuccess = { _, _ ->
                        ivFoto.animate()
                            .alpha(1f)
                            .setDuration(400)
                            .start()
                    }
                )
            }
        }

        view.findViewById<TextView>(R.id.tvNombrePlanta).text = nombreComun
        view.findViewById<TextView>(R.id.tvNombreCientifico).text = nombreCientifico

        view.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener {
            activity?.findViewById<View>(R.id.navMenu)?.visibility = View.VISIBLE
            parentFragmentManager.navigateClose(AddPlantFragment(), R.id.contenedorPrincipal)
        }

        view.findViewById<Button>(R.id.btnAtras).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnSiguientePaso2).setOnClickListener {
            val fotoUri = arguments?.getString("foto_uri")
            val nombreComun = arguments?.getString("nombre_comun")
            val nombreCientifico = arguments?.getString("nombre_cientifico")
            val tempId = arguments?.getString("temp_id")

            val bundle = Bundle().apply {
                putString("foto_uri", fotoUri)
                putString("nombre_comun", nombreComun)
                putString("nombre_cientifico", nombreCientifico)
                putString("temp_id", tempId)
            }

            val paso3 = AddPlantPt3Fragment()
            paso3.arguments = bundle
            parentFragmentManager.navigateTo(paso3, R.id.contenedorPrincipal)
        }
    }
}