package com.anthonystark.module.step.step9

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.anthonystark.module.R
import com.google.android.material.tabs.TabLayout

class Step9Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step9)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = SimpleAdapter(supportFragmentManager)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }

    private class SimpleAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val count = TAB_TITLES.size

        override fun getCount(): Int = TAB_TITLES.size
        override fun getPageTitle(position: Int): CharSequence? {
            return TAB_TITLES[position]
        }

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return Step9SimpleFragment()
                1 -> return Step9AccordionFragment()
                2 -> return Step9RecyclerViewFragment()
                3 -> return Step9HorizontalFragment()
                4 -> return Step9ManualFragment()
            }
            throw IllegalStateException("There's no fragment for position $position")
        }

    }

    companion object {
        private val TAB_TITLES = arrayOf(
            "Simple",
            "Accordion",
            "Recycler",
            "Horizontal",
            "Manual"
        )
    }

}