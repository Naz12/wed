package com.teknosols.a3orrsy.view.adapters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import com.teknosols.a3orrsy.datamodel.model.fan.Booking
import java.util.ArrayList

class AdminHomeTabAdapter: FragmentStatePagerAdapter {

    private var mFragmentList = ArrayList<Fragment>()
    private var mFragmentTitleList = ArrayList<String>()

    lateinit var fragment: Fragment
    lateinit var bundle: Bundle

    lateinit var inProgress : ArrayList<Booking>
    lateinit var completed : ArrayList<Booking>
    lateinit var cancelled : ArrayList<Booking>

    constructor(fragmentManager: FragmentManager) : super(fragmentManager)


    override fun getItem(position: Int): Fragment {
        when (position){
            0 ->{
                fragment = mFragmentList[position]
                bundle = Bundle()
                bundle.putSerializable("bookings", inProgress)
                fragment.arguments = bundle
            }
            1 ->{
                fragment = mFragmentList[position]
                bundle = Bundle()
                bundle.putSerializable("bookings", completed)
                fragment.arguments = bundle
            }
            2 ->{
                fragment = mFragmentList[position]
                bundle = Bundle()
                bundle.putSerializable("bookings", cancelled)
                fragment.arguments = bundle
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

    fun addFragment(fragment: Fragment, title: String, bookings: ArrayList<Booking>){
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
        inProgress = ArrayList()
        cancelled = ArrayList()
        completed = ArrayList()

        splitBookings(bookings)
    }

    fun splitBookings(bookings: ArrayList<Booking>){
        for(x in 0 until bookings.size){
            when(bookings.get(x).booking_status){
                "disabled " -> {            // 0

                }
                "inprogress" -> {           // 1
                    inProgress.add(bookings.get(x))
                }
                "advance_paid" -> {         // 2
                    inProgress.add(bookings.get(x))
                }
                "fully_paid" -> {           // 3
                    inProgress.add(bookings.get(x))
                }
                "completed" ->{             // 4
                    completed.add(bookings.get(x))
                }
                "cancelled_by_client" ->{   // 5
                    cancelled.add(bookings.get(x))
                }
                "cancelled_non_payment" ->{   // 6
                    cancelled.add(bookings.get(x))
                }
                else ->{
                    Log.e("AdminHomeTabAdapter", "Invalid Status Found In Data")
                }
            }
        }
    }


}