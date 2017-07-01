package com.kamontat.checkidnumber.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import java.util.*

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Thu 11/May/2017 - 10:19 PM
 */
class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val fragmentList: MutableList<Fragment>

    init {
        this.fragmentList = ArrayList<Fragment>()
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}
