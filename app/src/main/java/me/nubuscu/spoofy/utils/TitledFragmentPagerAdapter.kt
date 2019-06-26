package me.nubuscu.spoofy.utils

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TitledFragmentPagerAdapter(fm: FragmentManager, private val fragments: List<TitledFragment>) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int) = fragments[position].title
}