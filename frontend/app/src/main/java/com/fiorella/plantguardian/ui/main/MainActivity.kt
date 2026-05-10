package com.fiorella.plantguardian.ui.main
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.ui.add_plant.AddPlantFragment
import com.fiorella.plantguardian.ui.add_plant.AddPlantPt1Fragment
import com.fiorella.plantguardian.ui.add_plant.AddPlantPt2Fragment
import com.fiorella.plantguardian.ui.add_plant.AddPlantPt3Fragment
import com.fiorella.plantguardian.ui.add_plant.AddPlantPt4Fragment
import com.fiorella.plantguardian.ui.add_plant.AddPlantPt5Fragment
import com.fiorella.plantguardian.ui.my_plants.MyPlantsFragment
import com.fiorella.plantguardian.ui.my_plants.ViewPlantFragment
import com.fiorella.plantguardian.ui.user.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val fragmentsSinNav = setOf(
        AddPlantPt1Fragment::class.java,
        AddPlantPt2Fragment::class.java,
        AddPlantPt3Fragment::class.java,
        AddPlantPt4Fragment::class.java,
        AddPlantPt5Fragment::class.java,
        ViewPlantFragment::class.java
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            cargarFragmento(HomeFragment(), "Inicio")
        }

        supportFragmentManager.addOnBackStackChangedListener {
            actualizarNavMenu()
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.navMenu)
        bottomNavigation.setOnItemSelectedListener { item ->
            val (fragment, tag) = when (item.itemId) {
                R.id.nav_home -> HomeFragment() to "Inicio"
                R.id.nav_add -> AddPlantFragment() to "Añadir"
                R.id.nav_plants -> MyPlantsFragment() to "Plantas"
                R.id.nav_profile -> UserFragment() to "Perfil"
                else -> HomeFragment() to "Inicio"
            }
            cargarFragmento(fragment, tag)
            true
        }
    }

    fun cargarFragmento(fragment: Fragment, tag: String) {
        if (fragmentsSinNav.contains(fragment::class.java)) ocultarNav() else mostrarNav()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.contenedorPrincipal, fragment, tag)
            .commit()
    }

    fun ocultarNav() {
        val nav = findViewById<View>(R.id.navMenu)
        if (nav.visibility == View.VISIBLE) {
            nav.animate()
                .alpha(0f)
                .setDuration(150)
                .withEndAction { nav.visibility = View.INVISIBLE }
                .start()
        }
    }

    fun mostrarNav() {
        val nav = findViewById<View>(R.id.navMenu)
        if (nav.visibility == View.INVISIBLE) {
            nav.visibility = View.VISIBLE
            nav.alpha = 0f
            nav.animate()
                .alpha(1f)
                .setStartDelay(150)
                .setDuration(150)
                .start()
        }
    }

    private fun actualizarNavMenu() {
        val actual = supportFragmentManager.findFragmentById(R.id.contenedorPrincipal)
        if (actual != null && fragmentsSinNav.contains(actual::class.java)) {
            ocultarNav()
        } else {
            mostrarNav()
        }
    }
}