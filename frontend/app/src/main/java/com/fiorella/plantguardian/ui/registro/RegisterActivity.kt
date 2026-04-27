package com.fiorella.plantguardian.ui.registro
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.RegisterRequest
import com.fiorella.plantguardian.data.network.RetrofitClient
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        configurarNavegacionAtras()
        configurarRegistro()
    }

    private fun configurarNavegacionAtras() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun configurarRegistro() {
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val etNombre = findViewById<EditText>(R.id.etRegistroNombre)
        val etCorreo = findViewById<EditText>(R.id.etRegistroCorreo)
        val etCiudad = findViewById<EditText>(R.id.etRegistroCiudad)
        val etContra = findViewById<EditText>(R.id.etRegistroContra)
        val etRepiteContra = findViewById<EditText>(R.id.etRegistroRepiteContra)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val ciudad = etCiudad.text.toString().trim()
            val contra = etContra.text.toString().trim()
            val repiteContra = etRepiteContra.text.toString().trim()

            if (nombre.isEmpty() || correo.isEmpty() || ciudad.isEmpty() || contra.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (contra != repiteContra) {
                Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ejecutarRegistro(nombre, correo, ciudad, contra)
        }
    }

    private fun ejecutarRegistro(nombre: String, correo: String, ciudad: String, contra: String) {
        lifecycleScope.launch {
            try {
                val request = RegisterRequest(
                    nombre = nombre,
                    correo = correo,
                    password = contra,
                    ubicacion = ciudad
                )
                val response = RetrofitClient.instance.registro(request)
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "¡Cuenta creada con éxito!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Error al crear la cuenta.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}