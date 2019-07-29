package com.platform.lynch.servo.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.platform.lynch.servo.activity.*


class TabAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    private val title = arrayOf("Scan", "Menu", "History", "Profile")
    val fragments = arrayOf(
            ScanTab.getInstance(0),
            MenuTab.getInstance(1),
            TicketTab.getInstance(2),
            ProfileTab.getInstance(3)
        )

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return title.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return title[position]
    }

}