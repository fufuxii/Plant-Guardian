package com.fiorella.plantguardian.ui.login
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.EditText
import android.widget.TextView
import android.text.Spanned
import android.widget.Toast
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.LoginRequest
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.ui.main.MainActivity
import kotlinx.coroutines.launch
import coil.load

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configurar_app_logo()
        configurar_registro_txt()
        configurarBotonEntrar()
    }

    private fun configurar_app_logo() {
        val ivLogo = findViewById<ImageView>(R.id.ivLogo)
        val urlLogo = "https://zzfvpteyfvghpvyfhekj.supabase.co/storage/v1/object/public/app/logo_plant_guardian.png"
        ivLogo.load(urlLogo) {
            crossfade(true)
        }
    }

    private fun configurar_registro_txt() {
        val tvRegistro = findViewById<TextView>(R.id.tvRegistro)
        val registro_txt = "¿No tienes cuenta? Crea una"
        val spannable = SpannableString(registro_txt)
        val registro_txt_inicio = registro_txt.indexOf("Crea una")
        val registro_txt_fin = registro_txt_inicio + "Crea una".length
        val color_verde_principal = ContextCompat.getColor(this, R.color.verde_principal)

        spannable.setSpan(UnderlineSpan(), registro_txt_inicio, registro_txt_fin, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(color_verde_principal), registro_txt_inicio, registro_txt_fin, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvRegistro.text = spannable
        tvRegistro.setOnClickListener {
            Toast.makeText(this, "Abriendo registro...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarBotonEntrar() {
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etContra = findViewById<EditText>(R.id.etContra)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)

        btnEntrar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val password = etContra.text.toString().trim()

            if (validar_campos(correo, password)) {
                ejecutar_login(correo, password)
            }
        }
    }

    private fun validar_campos(correo: String, password: String): Boolean {
        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa los campos.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun ejecutar_login(correo: String, password: String) {
        lifecycleScope.launch {
            try {
                val request = LoginRequest(correo = correo, password = password)
                val response = RetrofitClient.instance.login(request)

                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "¡Bienvenido/a!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}