package com.fiorella.plantguardian.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.fiorella.plantguardian.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val ivLogo = findViewById<ImageView>(R.id.ivLogo)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)

        val urlLogo = "https://zzfvpteyfvghpvyfhekj.supabase.co/storage/v1/object/public/app/logo_plant_guardian.png"

        ivLogo.load(urlLogo) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground) // Imagen mientras carga
            error(R.drawable.ic_launcher_background)      // Imagen si falla la carga
        }

        // 3. Configurar el evento del botón Entrar
        btnEntrar.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Aquí llamaremos a tu ViewModel para validar con el backend
                validarUsuario(email, pass)
            }
        }
    }

    private fun validarUsuario(email: String, pass: String) {
        // Por ahora, solo un aviso para verificar que el botón funciona
        Toast.makeText(this, "Conectando con el servidor...", Toast.LENGTH_SHORT).show()

        // El siguiente paso será configurar Retrofit aquí para enviar
        // los datos a tu FastAPI
    }
}