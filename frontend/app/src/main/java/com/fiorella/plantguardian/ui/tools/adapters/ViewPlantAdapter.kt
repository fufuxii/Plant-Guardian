package com.fiorella.plantguardian.ui.tools.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fiorella.plantguardian.data.schemas.PlantData
import com.fiorella.plantguardian.ui.my_plants.sections.ViewPlantDiagnosisFragment
import com.fiorella.plantguardian.ui.my_plants.sections.ViewPlantInfoFragment
import com.fiorella.plantguardian.ui.my_plants.sections.ViewPlantTasksFragment

class ViewPlantAdapter(
    fragment: Fragment,
    private val planta: PlantData?
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ViewPlantTasksFragment.newInstance(planta)
        1 -> ViewPlantDiagnosisFragment.newInstance(planta)
        2 -> ViewPlantInfoFragment.newInstance(planta)
        else -> throw IllegalStateException("Posición inválida")
    }
}