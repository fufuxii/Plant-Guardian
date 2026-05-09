package com.fiorella.plantguardian.ui.add_plant
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.PlantResponse
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.ui.extensions.navigateClose
import com.fiorella.plantguardian.ui.extensions.navigateTo
import com.fiorella.plantguardian.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Suppress("DEPRECATION")
class AddPlantPt1Fragment : Fragment() {

    private var imgCapturada: Uri? = null

    private val camaraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
        if (exito) { mostrarImagenConFade(imgCapturada) }
    }

    private val galeriaLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imgCapturada = uri
            mostrarImagenConFade(uri)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_plant_pt1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.ocultarNav()

        view.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener {
            (activity as? MainActivity)?.mostrarNav()
            parentFragmentManager.navigateClose(AddPlantFragment(), R.id.contenedorPrincipal)
        }

        view.findViewById<ImageView>(R.id.btnGaleria).setOnClickListener {
            galeriaLauncher.launch("image/*")
        }

        view.findViewById<ImageView>(R.id.btnCamara).setOnClickListener {
            try {
                imgCapturada = crearArchivoConFoto()
                camaraLauncher.launch(imgCapturada)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        view.findViewById<Button>(R.id.btnSiguientePaso1).setOnClickListener {
            if (imgCapturada != null) {
                enviarImagen(imgCapturada!!)
            } else {
                Toast.makeText(requireContext(), "Es necesario una foto o imagen de la planta.", Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun enviarImagen(uri: Uri) {
        val layoutCarga = view?.findViewById<View>(R.id.layoutCargando)
        layoutCarga?.visibility = View.VISIBLE

        val file = File(requireContext().cacheDir, "temp_plant.jpg")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        file.writeBytes(inputStream?.readBytes() ?: return)

        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("imagen", file.name, requestFile)

        RetrofitClient.instance.identificarPlanta(body).enqueue(object : retrofit2.Callback<PlantResponse> {
            override fun onResponse(call: retrofit2.Call<PlantResponse>, response: retrofit2.Response<PlantResponse>) {
                val respuesta = response.body()
                val datosPlanta = respuesta?.resultado

                if (response.isSuccessful && datosPlanta?.error == null && datosPlanta != null) {
                    redireccionarAlSiguientePaso(uri, respuesta)
                } else {
                    layoutCarga?.visibility = View.GONE
                    val errorMsg = datosPlanta?.error ?: "No se reconoció la planta."
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<PlantResponse>, t: Throwable) {
                layoutCarga?.visibility = View.GONE
                Toast.makeText(requireContext(), "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun redireccionarAlSiguientePaso(uri: Uri, respuesta: PlantResponse) {
        val bundle = Bundle().apply {
            putString("foto_uri", uri.toString())
            putString("nombre_comun", respuesta.resultado?.nombre_comun)
            putString("nombre_cientifico", respuesta.resultado?.nombre_cientifico)
            putString("temp_id", respuesta.temp_id)
        }

        val paso2 = AddPlantPt2Fragment()
        paso2.arguments = bundle
        parentFragmentManager.navigateTo(paso2, R.id.contenedorPrincipal)
    }

    private fun mostrarImagenConFade(uri: Uri?) {
        val ivPreview = view?.findViewById<ImageView>(R.id.ivImagenCapturada) ?: return
        val llBotones = view?.findViewById<View>(R.id.llContenedorBotones) ?: return

        ivPreview.alpha = 0f
        ivPreview.visibility = View.VISIBLE

        ivPreview.load(uri) {
            listener(
                onSuccess = { _, _ ->
                    val params = llBotones.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
                    params.topToBottom = R.id.ivImagenCapturada
                    params.bottomToTop = R.id.botones
                    params.verticalBias = 0f
                    params.topMargin = (25 * resources.displayMetrics.density).toInt()
                    llBotones.layoutParams = params
                    ivPreview.animate()
                        .alpha(1f)
                        .setDuration(400)
                        .start()
                }
            )
        }
    }
}