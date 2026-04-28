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
import com.fiorella.plantguardian.ui.register.RegisterActivity
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configurarAppLogo()
        configurarRegistroTexto()
        configurarRegistroBoton()
    }

    private fun configurarAppLogo() {
        val ivLogo = findViewById<ImageView>(R.id.ivLogoApp)
        val urlLogo = "https://zzfvpteyfvghpvyfhekj.supabase.co/storage/v1/object/public/app/logo_plant_guardian.png"
        ivLogo.load(urlLogo) {
            crossfade(true)
        }
    }

    private fun configurarRegistroTexto() {
        val tvRegistro = findViewById<TextView>(R.id.tvRegistro)
        val registroTxt = "¿No tienes cuenta? Crea una"
        val spannable = SpannableString(registroTxt)
        val registroTxtInicio = registroTxt.indexOf("Crea una")
        val registroTxtFin = registroTxtInicio + "Crea una".length
        val colorVerdePrincipal = ContextCompat.getColor(this, R.color.verde_principal)

        spannable.setSpan(UnderlineSpan(), registroTxtInicio, registroTxtFin, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(colorVerdePrincipal), registroTxtInicio, registroTxtFin, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvRegistro.text = spannable
        tvRegistro.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configurarRegistroBoton() {
        val etCorreo = findViewById<EditText>(R.id.etLoginCorreo)
        val etContra = findViewById<EditText>(R.id.etLoginContra)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)

        btnEntrar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val password = etContra.text.toString().trim()

            if (validarCampos(correo, password)) {
                ejecutarLogin(correo, password)
            }
        }
    }

    private fun validarCampos(correo: String, password: String): Boolean {
        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa los campos.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun ejecutarLogin(correo: String, password: String) {
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