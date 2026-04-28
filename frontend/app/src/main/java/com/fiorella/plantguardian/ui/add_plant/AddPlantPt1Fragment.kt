package com.fiorella.plantguardian.ui.add_plant
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R
import java.io.File

@Suppress("DEPRECATION")
class AddPlantPt1Fragment : Fragment() {

    private var imgCapturada: Uri? = null

    private val camaraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
        if (exito) {
            view?.findViewById<ImageView>(R.id.btnCamara)?.setImageURI(imgCapturada)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.GONE

        view.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorPrincipal, AddPlantFragment())
                .commit()
        }

        view.findViewById<Button>(R.id.btnSiguientePaso1).setOnClickListener {
            if (imgCapturada != null) {
                val bundle = Bundle().apply {
                    putString("foto_uri", imgCapturada.toString())
                }
                val paso2 = AddPlantPt2Fragment()
                paso2.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.contenedorPrincipal, paso2)
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Toma una foto primero", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<ImageView>(R.id.btnCamara).setOnClickListener {
            try {
                imgCapturada = crearArchivoConFoto()
                camaraLauncher.launch(imgCapturada)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("CAMARA_DEBUG", "Error detallado:", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.navMenu)?.visibility = View.VISIBLE
    }

    private fun crearArchivoConFoto(): Uri {
        val nombre = "PLANT_${System.currentTimeMillis()}_"
        val directorio = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        if (directorio?.exists() == false) {
            directorio.mkdirs()
        }

        val archivo = File.createTempFile(nombre, ".jpg", directorio)

        return FileProvider.getUriForFile(
            requireContext(),
            "com.fiorella.plantguardian.fileprovider",
            archivo
        )
    }
}