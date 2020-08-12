package com.teknosols.a3orrsy.view.activites

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.common.eventbus.Subscribe
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Booking
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.other.eventbus.EventBusFactory
import com.teknosols.a3orrsy.other.extensions.gotoLandingActivity
import com.teknosols.a3orrsy.view.adapters.AdminItemMenuAdapter
import com.teknosols.a3orrsy.view.adapters.UserNotificationMenuAdapter
import com.teknosols.a3orrsy.view.fragments.AdminBookingDetailFragment
import com.teknosols.a3orrsy.view.fragments.AdminBookingsFragment
import com.teknosols.a3orrsy.view.fragments.AdminHomeFragment
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_admin.drawer_layout
import kotlinx.android.synthetic.main.activity_admin.nav_view
import kotlinx.android.synthetic.main.activity_admin.toolbarLayout
import kotlinx.android.synthetic.main.activity_global_navigation.*
import kotlinx.android.synthetic.main.navigation_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*


class AdminActivity : BaseActivity() , NavigationView.OnNavigationItemSelectedListener, AdminItemMenuAdapter.ItemClickListener, View.OnClickListener {
    override fun openDetail(category: Category) {
        drawer_layout.closeDrawers()
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
        }
    }

    lateinit var adapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        toolbarLayout.header_textview.text = getString(R.string.booking)


        if(!sessionManager.getProfileImage().isNullOrEmpty()) {
            Glide.with(this)
                .load(sessionManager.getProfileImage())
                .apply(RequestOptions.circleCropTransform())
                .into(ivUserImage)
        }

        tvProfileName.text = sessionManager.getFirstName()

        val adminHomeFragment = AdminHomeFragment.newInstance()
        replaceFragment(adminHomeFragment,adminHomeFragment.getSimpleName(),false,true)

        nav_view.setNavigationItemSelectedListener(this)

        adapter = AdminItemMenuAdapter(this)
        rlItemsMain?.let {
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }

        ratecard_header_back_layout.setOnClickListener(this)
        log_out.setOnClickListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun openDrawer(){
        drawer_layout.openDrawer(nav_view)
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

    @Subscribe
    fun onEventChange(str: String) {
        runOnUiThread {

            //NOTIFY Current Fragment
            if (supportFragmentManager.findFragmentById(R.id.fragment_container) != null) {
                val f = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if(f is AdminHomeFragment){
                    f.setAdapter()
                }
            }
        }
    }

}