package com.example.nvtrong.appenglish.tab

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList

class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    var arrayList = ArrayList<Fragment>()
    var arrayListTitle = ArrayList<String>()
    override fun getItem(position: Int): Fragment {
        return arrayList.get(position)
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        arrayList.add(fragment)
        arrayListTitle.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return arrayListTitle.get(position)
    }
}
