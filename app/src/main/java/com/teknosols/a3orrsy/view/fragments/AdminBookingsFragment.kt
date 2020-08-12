package com.teknosols.a3orrsy.view.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Booking
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.datamodel.model.fan.SubCategory
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.adapters.AdminBookingsAdapter
import com.teknosols.a3orrsy.view.adapters.CategoryAdapter
import com.teknosols.a3orrsy.view.decoration.ListSpacingDecoration
import com.teknosols.a3orrsy.viewmodel.CategoriesViewModel
import kotlinx.android.synthetic.main.fragment_admin_bookings.*


class AdminBookingsFragment : BaseFragment(), View.OnClickListener, AdminBookingsAdapter.ItemClickListener{
    override fun openDetail(booking: Booking) {
        val adminBookingDetailFragment = AdminBookingDetailFragment.newInstance(booking)
        (activity as BaseActivity).replaceFragment(adminBookingDetailFragment,adminBookingDetailFragment.getSimpleName(),true,false)
    }

    lateinit var adapter: AdminBookingsAdapter
    lateinit var bookings: ArrayList<Booking>
//    lateinit var status: String

    override fun onClick(v: View?) {
        when(v) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
//            this.status = it!!["status"] as String
            this.bookings = it!!["bookings"] as ArrayList<Booking>
        }
//        Log.i("MyStatus", status)
//        Toast.makeText(context!!, status , Toast.LENGTH_SHORT).show()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_admin_bookings, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }


    fun setAdapter(){
        adapter = AdminBookingsAdapter(context!!,bookings,this)
        rvBookings?.let {
            it.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            it.addItemDecoration(ListSpacingDecoration(resources.getDimensionPixelSize(R.dimen.recycler_view_grid_spacing)))
            it.adapter = adapter
            if(it.itemDecorationCount > 1){ it.removeItemDecorationAt(0) }
        }
    }

    companion object {


        @JvmStatic
        fun newInstance()= AdminBookingsFragment()
    }



}
