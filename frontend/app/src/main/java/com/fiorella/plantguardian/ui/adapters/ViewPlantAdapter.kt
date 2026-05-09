package com.fiorella.plantguardian.ui.adapters
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fiorella.plantguardian.data.model.PlantData
import com.fiorella.plantguardian.ui.my_plants.ViewPlantDiagnosisFragment
import com.fiorella.plantguardian.ui.my_plants.ViewPlantInfoFragment
import com.fiorella.plantguardian.ui.my_plants.ViewPlantTasksFragment

class ViewPlantAdapter(
    fragment: Fragment,
    private val planta: PlantData?
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ViewPlantTasksFragment.newInstance(planta)
        1 -> ViewPlantDiagnosisFragment()
        2 -> ViewPlantInfoFragment()
        else -> throw IllegalStateException("Posición inválida")
    }
}