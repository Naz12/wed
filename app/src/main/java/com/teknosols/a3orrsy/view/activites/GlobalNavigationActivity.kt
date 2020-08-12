package com.teknosols.a3orrsy.view.activites

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.befikey.user.view.services.MyFirebaseMessagingService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Booking
import com.teknosols.a3orrsy.datamodel.model.fan.Notifications
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.AllBookingsResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.BookingResponse
import com.teknosols.a3orrsy.other.eventbus.EventBusFactory
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.gotoLandingActivity
import com.teknosols.a3orrsy.other.extensions.obtainViewModel
import com.teknosols.a3orrsy.view.adapters.UserItemMenuAdapter
import com.teknosols.a3orrsy.view.adapters.UserNotificationMenuAdapter
import com.teknosols.a3orrsy.view.fragments.AdminBookingDetailFragment
import com.teknosols.a3orrsy.view.fragments.CategoriesFragment
import com.teknosols.a3orrsy.view.fragments.HomeFragment
import com.teknosols.a3orrsy.viewmodel.AdminHomeViewModel
import kotlinx.android.synthetic.main.activity_global_navigation.*
import kotlinx.android.synthetic.main.activity_global_navigation.view.*
import kotlinx.android.synthetic.main.navigation_layout.*
import kotlinx.android.synthetic.main.notification_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.security.acl.Owner

class GlobalNavigationActivity : BaseActivity() , NavigationView.OnNavigationItemSelectedListener, View.OnClickListener , UserNotificationMenuAdapter.ItemClickListener{
    override fun openDetail(booking: Booking) {
        drawer_layout.closeDrawers()

        if(booking.notifications.get(0).is_seen == 0){
            viewModel.updateNotificationStatus(sessionManager.getUserId(),booking.notifications.get(0).id.toString(),"1");
        }

        val adminBookingDetailFragment = AdminBookingDetailFragment.newInstance(booking)
        replaceFragment(adminBookingDetailFragment , adminBookingDetailFragment.getSimpleName() , true , false)
    }

    override fun onClick(v: View?) {
        when(v) {
            ratecard_header_back_layout -> {
                openDrawer()
            }
            log_out ->{
                sessionManager.logout()
                gotoLandingActivity()
            }
            rlNotificationRing ->{
                openNotificationDrawer()
            }
        }
    }

    lateinit var viewModel: AdminHomeViewModel
    lateinit var bookingResponse: ArrayList<Booking>
    lateinit var adapter: RecyclerView.Adapter<*>
    lateinit var notificationAdapter : RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_navigation)

        if(!sessionManager.getProfileImage().isNullOrEmpty()) {
            Glide.with(this)
                .load(sessionManager.getProfileImage())
                .apply(RequestOptions.circleCropTransform())
                .into(ivUserImage)
        }

        tvProfileName.text = sessionManager.getFirstName()

        val homeFragment = HomeFragment.newInstance()
        replaceFragment(homeFragment , HomeFragment.javaClass.simpleName , false , true)

        nav_view.setNavigationItemSelectedListener(this)

        adapter = UserItemMenuAdapter(this)
        rlItemsMain?.let {
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }

        viewModelHandler()
        ratecard_header_back_layout.setOnClickListener(this)
        log_out.setOnClickListener(this)
        rlNotificationRing.setOnClickListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun openDrawer(){
        drawer_layout.openDrawer(nav_view)
    }

    fun openNotificationDrawer(){
        drawer_layout.openDrawer(nav_view_notifications)
    }

    fun checkNotificationCount(){

        var notificationCount = 0

        for(x in 0 until bookingResponse.size){
            if(bookingResponse.get(x).notifications.get(0).is_seen == 0){
                ++notificationCount
            }
        }

        if(notificationCount != 0){

            if(notificationCount > 99){
                tvNumberNotification.text = "99+"
                tvNumberNotification.visibility = View.VISIBLE
            } else{
                tvNumberNotification.text = notificationCount.toString()
                tvNumberNotification.visibility = View.VISIBLE
            }

        }else{
            tvNumberNotification.visibility = View.GONE
        }

    }

    fun viewModelHandler(){

        viewModel = obtainViewModel(AdminHomeViewModel::class.java)

        viewModel.getAllUserNotifications(sessionManager.getUserId())

        viewModel.allBookingsResponse.observe(LifecycleOwner({lifecycle}), object: Observer<OneShotEvent<AllBookingsResponse>>{
            override fun onChanged(t: OneShotEvent<AllBookingsResponse>?) {

                var show = t?.getContentIfNotHandled()
                show?.let {
                    bookingResponse = it.data
                    setAdapter()
                }
            }
        })

        viewModel.notificationResponse.observe(LifecycleOwner({lifecycle}), object: Observer<OneShotEvent<Notifications>>{
            override fun onChanged(t: OneShotEvent<Notifications>?) {

                var show = t?.getContentIfNotHandled()
                show?.let {
                    viewModel.getAllUserNotifications(sessionManager.getUserId())
                }
            }
        })

    }

    private fun setAdapter(){

        notificationAdapter = UserNotificationMenuAdapter(this , bookingResponse , this)
        rlItemsNotification?.let{
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.adapter = notificationAdapter
        }
        checkNotificationCount()
    }

    @Subscribe
    fun getMessage(status: String){
        runOnUiThread {

            Log.e("Testing" , status)
            viewModel.getAllUserNotifications(sessionManager.getUserId())
        }
    }

    override fun onStart() {
        super.onStart()
        EventBusFactory.getEventBus().register(this)
    }

    override fun onPause() {
        super.onPause()
        try {
            EventBusFactory.getEventBus().unregister(this)
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        fragment!!.onActivityResult(requestCode, resultCode, data)
    }

}