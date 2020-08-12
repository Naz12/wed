package com.teknosols.a3orrsy.view.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Booking
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.AllBookingsResponse
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.obtainViewModel
import com.teknosols.a3orrsy.view.activites.AdminActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.adapters.AdminHomeTabAdapter
import com.teknosols.a3orrsy.viewmodel.AdminHomeViewModel
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.fragment_admin_home.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*


class AdminHomeFragment : BaseFragment(), View.OnClickListener {

    lateinit var adapter: AdminHomeTabAdapter
    lateinit var viewModel: AdminHomeViewModel
    lateinit var bookings: ArrayList<Booking>
    lateinit var loginAs: String
    lateinit var userId: String

    override fun onClick(v: View?) {
        when (v) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginAs = sessionManager.getLoginAs()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_admin_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (loginAs.equals("customer")){
            (activity as GlobalNavigationActivity).toolbarLayout.header_textview.text = getString(R.string.booking)
            (activity as GlobalNavigationActivity).toolbarLayout.rlNotificationRing.visibility = View.VISIBLE
        }else{
            (activity as AdminActivity).toolbarLayout.header_textview.text = getString(R.string.booking)
            (activity as AdminActivity).toolbarLayout.rlNotificationRing.visibility = View.GONE
        }

        viewModelHandler()

        swiperefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if (loginAs.equals("customer")) {

                userId = sessionManager.getUserId()
                viewModel.getAllUserBookings(userId)

            } else {

                viewModel.getAllBookings()
            }

            swiperefresh.setRefreshing(false)
        })
    }

    fun viewModelHandler() {
        viewModel = obtainViewModel(AdminHomeViewModel::class.java)

        if (loginAs.equals("customer")) {

            userId = sessionManager.getUserId()
            viewModel.getAllUserBookings(userId)

        } else {

            viewModel.getAllBookings()
        }

        viewModel.snackbarMessage.observe(viewLifecycleOwner, object : Observer<OneShotEvent<String>> {
            override fun onChanged(t: OneShotEvent<String>?) {
                var msg = t?.getContentIfNotHandled()
                if (!msg.isNullOrEmpty())
                    showAlertDialog(msg)
            }
        })

        viewModel.progressBar.observe(viewLifecycleOwner, object : Observer<OneShotEvent<Boolean>> {
            override fun onChanged(t: OneShotEvent<Boolean>?) {
                var show = t?.getContentIfNotHandled()
                if (show != null)
                    showProgressDialog(show)
            }
        })

        viewModel.allBookingsResponse.observe(viewLifecycleOwner, object : Observer<OneShotEvent<AllBookingsResponse>> {
            override fun onChanged(t: OneShotEvent<AllBookingsResponse>?) {
                var show = t?.getContentIfNotHandled()
                show?.let {
                    bookings = it.data
                    setAdapter()
                }
            }
        })
    }

    fun setAdapter() {
        adapter = AdminHomeTabAdapter(childFragmentManager!!)
        adapter.addFragment(AdminBookingsFragment(), getString(R.string.in_process), bookings)
        adapter.addFragment(AdminBookingsFragment(), getString(R.string.completed), bookings)
        adapter.addFragment(AdminBookingsFragment(), getString(R.string.cancelled), bookings)
        viewpager.adapter = adapter
        tabs.setupWithViewPager(viewpager)
    }


    companion object {
        @JvmStatic
        fun newInstance() = AdminHomeFragment()
    }


}
