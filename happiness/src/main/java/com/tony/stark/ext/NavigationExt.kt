package com.tony.stark.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


val FragmentManager.currentNavigationFragment: Fragment? get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()

fun FragmentManager.refreshFragment(currentFragment: Fragment) {
    val childFragmentManager = primaryNavigationFragment?.childFragmentManager
    childFragmentManager?.beginTransaction()
        ?.detach(currentFragment)
        ?.attach(currentFragment.apply {})
        ?.commitAllowingStateLoss()
}

