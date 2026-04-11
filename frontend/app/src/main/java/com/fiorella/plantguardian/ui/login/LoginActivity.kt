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

        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etContra = findViewById<EditText>(R.id.etContra)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val tvRegistro = findViewById<TextView>(R.id.tvRegistro)

        btnEntrar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val contra = etContra.text.toString().trim()

            if (correo.isEmpty() || contra.isEmpty()) {
                Toast.makeText(this, "Por favor, completa los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val request = LoginRequest(correo = correo, password = contra)
                    val response = RetrofitClient.instance.login(request)

                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        Toast.makeText(this@LoginActivity, "¡Bienvenido/a!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        tvRegistro.setOnClickListener {
            Toast.makeText(this, "Abriendo registro...", Toast.LENGTH_SHORT).show()
        }
    }
}