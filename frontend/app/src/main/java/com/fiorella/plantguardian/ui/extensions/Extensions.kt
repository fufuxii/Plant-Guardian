package com.fiorella.plantguardian.ui.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.fiorella.plantguardian.R

fun FragmentManager.navigateTo(
    fragment: Fragment,
    containerId: Int,
    addToBackStack: Boolean = true
) {
    beginTransaction()
        .setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        .replace(containerId, fragment)
        .apply { if (addToBackStack) addToBackStack(null) }
        .commit()
}

fun FragmentManager.navigateClose(
    fragment: Fragment,
    containerId: Int
) {
    beginTransaction()
        .setReorderingAllowed(true)
        .setCustomAnimations(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        .replace(containerId, fragment)
        .commit()
}

fun FragmentManager.navigateWithFade(
    fragment: Fragment,
    containerId: Int,
    addToBackStack: Boolean = true
) {
    beginTransaction()
        .setReorderingAllowed(true)
        .setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.fade_out
        )
        .replace(containerId, fragment)
        .apply { if (addToBackStack) addToBackStack(null) }
        .commit()
}