package com.fiorella.plantguardian.ui.anadir_planta
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.ui.main.AddPlantFragment

class AddPlantPt1Fragment : Fragment() {
    private val camaraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        if (bitmap != null) {
            view?.findViewById<ImageView>(R.id.btnCamara)?.setImageBitmap(bitmap)
            Log.d("CAMERA_DEBUG", "Foto capturada con éxito.")
        }
    }

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

        view.findViewById<ImageView>(R.id.btnCamara).setOnClickListener {
            val permissionCheck = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                camaraLauncher.launch(null)
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.VISIBLE
    }
}