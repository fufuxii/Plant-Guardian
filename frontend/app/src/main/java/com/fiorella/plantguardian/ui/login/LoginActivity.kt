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
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.LoginRequest
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.ui.main.MainActivity
import kotlinx.coroutines.launch
import coil.load
import com.fiorella.plantguardian.ui.registro.RegistroActivity
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configurar_app_logo()
        configurar_registro_txt()
        configurar_boton_entrar()
    }

    private fun configurar_app_logo() {
        val ivLogo = findViewById<ImageView>(R.id.ivLogoApp)
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
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configurar_boton_entrar() {
        val etCorreo = findViewById<EditText>(R.id.etLoginCorreo)
        val etContra = findViewById<EditText>(R.id.etLoginContra)
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
                val response = RetrofitClient.instance.login(LoginRequest(correo, password))

                if (response.isSuccessful && response.body() != null) {
                    val loginRes = response.body()!!
                    val uuidUsuario = loginRes.usuario?.id

                    if (uuidUsuario != null) {
                        getSharedPreferences("PlantGuardianPrefs", MODE_PRIVATE)
                            .edit {
                                putString("user_id", uuidUsuario)
                            }
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Credenciales incorrectas.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LOGIN_ERROR", "Fallo de conexión: ${e.message}")
            }
        }
    }
}