package com.fiorella.plantguardian.ui.main
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            loadFragment(InicioFragment(), "Inicio")
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> InicioFragment()
                R.id.nav_add -> AnadirPlantaFragment()
                R.id.nav_plants -> Fragment()
                R.id.nav_profile -> Fragment()
                else -> InicioFragment()
            }

            val tag = when (item.itemId) {
                R.id.nav_home -> "Inicio"
                R.id.nav_add -> "Añadir"
                R.id.nav_plants -> "Plantas"
                R.id.nav_profile -> "Perfil"
                else -> "Inicio"
            }

            loadFragment(selectedFragment, tag)
            true
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorPrincipal, fragment, tag)
            // .addToBackStack(null)
            .commit()
    }
}