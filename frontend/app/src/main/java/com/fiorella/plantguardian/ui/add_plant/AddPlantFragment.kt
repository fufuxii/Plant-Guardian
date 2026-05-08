package com.fiorella.plantguardian.ui.add_plant
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.ui.extensions.navigateTo

class AddPlantFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_plant, container, false)
    }

    override fun onResume() {
        super.onResume()
        val navMenu = activity?.findViewById<View>(R.id.navMenu)
        if (navMenu?.alpha == 0f) {
            navMenu.animate()
                .alpha(1f)
                .setStartDelay(150)
                .setDuration(100)
                .start()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnAdd = view.findViewById<CardView>(R.id.btnAnadirPlanta)
        btnAdd.setOnClickListener {
            parentFragmentManager.navigateTo(AddPlantPt1Fragment(), R.id.contenedorPrincipal)
        }
    }
}