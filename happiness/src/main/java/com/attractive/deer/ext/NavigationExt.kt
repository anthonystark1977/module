package com.attractive.deer.ext

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation


val FragmentManager.currentNavigationFragment: Fragment? get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()

fun FragmentManager.refreshFragment(currentFragment: Fragment) {
    val childFragmentManager = primaryNavigationFragment?.childFragmentManager
    childFragmentManager?.beginTransaction()
        ?.detach(currentFragment)
        ?.attach(currentFragment.apply {})
        ?.commitAllowingStateLoss()
}

fun Activity.navController(hostFragmentId: Int) = Navigation.findNavController(this, hostFragmentId)
