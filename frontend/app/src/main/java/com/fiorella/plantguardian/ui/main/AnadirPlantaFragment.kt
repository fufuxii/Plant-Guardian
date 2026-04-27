package com.fiorella.plantguardian.ui.main
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.fiorella.plantguardian.R

class AnadirPlantaFragment : Fragment () {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_anadir_planta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnAdd = view.findViewById<CardView>(R.id.btnStartAddProcess)
        btnAdd.setOnClickListener {
            Toast.makeText(requireContext(), "Iniciando proceso de reconocimiento...", Toast.LENGTH_SHORT).show()
            Log.d("AddPlantFragment", "Se pulsó el botón de Añadir.")
        }
    }
}