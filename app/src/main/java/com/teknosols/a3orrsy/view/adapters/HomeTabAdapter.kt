package com.teknosols.a3orrsy.view.adapters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.teknosols.a3orrsy.datamodel.model.fan.Booking
import java.util.ArrayList

class HomeTabAdapter : FragmentStatePagerAdapter {

    private var mFragmentList = ArrayList<Fragment>()
    private var mFragmentTitleList = ArrayList<String>()

    lateinit var fragment: Fragment
    lateinit var bundle: Bundle

    constructor(fragmentManager : FragmentManager ) : super(fragmentManager)


    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                fragment = mFragmentList[position]
            }
            1 -> {
                fragment = mFragmentList[position]
            }
        }
            return fragment
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

}