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
            cargarFragmento(HomeFragment(), "Inicio")
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.navMenu)
        bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_add -> AddPlantFragment()
                R.id.nav_plants -> Fragment()
                R.id.nav_profile -> Fragment()
                else -> HomeFragment()
            }

            val tag = when (item.itemId) {
                R.id.nav_home -> "Inicio"
                R.id.nav_add -> "Añadir"
                R.id.nav_plants -> "Plantas"
                R.id.nav_profile -> "Perfil"
                else -> "Inicio"
            }

            cargarFragmento(selectedFragment, tag)
            true
        }
    }

    private fun cargarFragmento(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorPrincipal, fragment, tag)
            // .addToBackStack(null)
            .commit()
    }
}