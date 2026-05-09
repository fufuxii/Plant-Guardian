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
import androidx.core.net.toUri
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.ui.extensions.navigateClose
import com.fiorella.plantguardian.ui.extensions.navigateTo
import com.fiorella.plantguardian.ui.main.MainActivity

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

        val fotoUri = arguments?.getString("foto_uri")
        val nombreComun = arguments?.getString("nombre_comun")
        val nombreCientifico = arguments?.getString("nombre_cientifico")

        configurarTextos(view, nombreComun, nombreCientifico)
        mostrarImagen(view, fotoUri)
        configurarBotones(view)
    }

    private fun configurarTextos(view: View, nombreComun: String?, nombreCientifico: String?) {
        view.findViewById<TextView>(R.id.tvNombrePlanta).text = nombreComun ?: "-"
        view.findViewById<TextView>(R.id.tvNombreCientifico).text = nombreCientifico ?: "-"
    }

    private fun mostrarImagen(view: View, fotoUri: String?) {
        fotoUri?.let { uriString ->
            val ivFoto = view.findViewById<ImageView>(R.id.ivImagenCapturada)
            ivFoto.alpha = 0f
            ivFoto.load(uriString.toUri()) {
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
    }

    private fun configurarBotones(view: View) {
        view.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener {
            cerrarFlujo()
        }

        view.findViewById<Button>(R.id.btnAtras).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnSiguientePaso2).setOnClickListener {
            redireccionarSiguientePaso()
        }
    }

    private fun cerrarFlujo() {
        (activity as? MainActivity)?.mostrarNav()
        parentFragmentManager.navigateClose(AddPlantFragment(), R.id.contenedorPrincipal)
    }

    private fun redireccionarSiguientePaso() {
        val bundle = Bundle().apply {
            putString("foto_uri", arguments?.getString("foto_uri"))
            putString("nombre_comun", arguments?.getString("nombre_comun"))
            putString("nombre_cientifico", arguments?.getString("nombre_cientifico"))
            putString("temp_id", arguments?.getString("temp_id"))
        }

        val paso3 = AddPlantPt3Fragment().apply {
            arguments = bundle
        }
        parentFragmentManager.navigateTo(paso3, R.id.contenedorPrincipal)
    }
}