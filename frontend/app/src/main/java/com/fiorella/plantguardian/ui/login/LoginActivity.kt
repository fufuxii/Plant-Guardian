package com.fiorella.plantguardian.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.LoginRequest
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.ui.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Enlazamos los componentes del XML con Kotlin
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etContra = findViewById<EditText>(R.id.etContra)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val tvGoToRegister = findViewById<TextView>(R.id.tvRegistro) // Verifica que este ID sea el del XML

        // 2. Configuramos el clic del botón Entrar
        btnEntrar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val contra = etContra.text.toString().trim()

            if (correo.isEmpty() || contra.isEmpty()) {
                Toast.makeText(this, "Por favor, completa los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Iniciamos la llamada a la API en una corrutina
            lifecycleScope.launch {
                try {
                    // Creamos el objeto con los datos
                    val request = LoginRequest(correo = correo, password = contra)

                    // Llamamos a la API a través de Retrofit
                    val response = RetrofitClient.instance.login(request)

                    if (response.isSuccessful) {
                        val loginResponse = response.body()

                        // Si el login es exitoso en tu FastAPI
                        Toast.makeText(this@LoginActivity, "¡Bienvenida, ${correo}!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Si el servidor responde pero con error (ej. 401 Unauthorized)
                        Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Si no hay internet o el servidor está apagado
                    Toast.makeText(this@LoginActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // 3. Ir al registro
        tvGoToRegister.setOnClickListener {
            // Intent para RegisterActivity cuando la tengas lista
            Toast.makeText(this, "Abriendo registro...", Toast.LENGTH_SHORT).show()
        }
    }
}