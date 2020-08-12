package com.teknosols.a3orrsy.view.fragments

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.other.extensions.gotoLandingActivity
import com.teknosols.a3orrsy.view.activites.AdminActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.activites.LandingActivity
import com.teknosols.a3orrsy.view.adapters.AdminHomeTabAdapter
import com.teknosols.a3orrsy.view.adapters.HomeTabAdapter
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_admin.toolbarLayout
import kotlinx.android.synthetic.main.custom_notification.view.*
import kotlinx.android.synthetic.main.fragment_admin_home.*
import kotlinx.android.synthetic.main.fragment_admin_home.swiperefresh
import kotlinx.android.synthetic.main.fragment_admin_home.tabs
import kotlinx.android.synthetic.main.fragment_admin_home.viewpager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.navigation_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import kotlinx.android.synthetic.main.toolbar_layout.view.ratecard_header_back_layout

class HomeFragment : BaseFragment(), View.OnClickListener {

    lateinit var adapter: HomeTabAdapter
    var firstCheck = false

    override fun onClick(v: View?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }

    private fun setToolbar() {
        (activity as GlobalNavigationActivity).toolbarLayout.header_textview.visibility = View.GONE
        (activity as GlobalNavigationActivity).toolbarLayout.ivlogo.visibility = View.VISIBLE
        (activity as GlobalNavigationActivity).toolbarLayout.ivNotificationRing.visibility = View.VISIBLE
        (activity as GlobalNavigationActivity).toolbarLayout.ivNotificationRing.setImageResource(R.drawable.ring)
        (activity as GlobalNavigationActivity).toolbarLayout.ivlogo.setImageResource(R.drawable.logo_white)
    }

    private fun setAdapter() {
        adapter = HomeTabAdapter(childFragmentManager!!)
        adapter.addFragment(FavEventFragment() , getString(R.string.home))
        adapter.addFragment(CategoriesFragment(), getString(R.string.booking))
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun onResume() {
        super.onResume()
        setToolbar()
//        if(firstCheck){
//            viewPager.setCurrentItem(0)
//        }

    }

    override fun onPause() {
        super.onPause()
        (activity as GlobalNavigationActivity).toolbarLayout.header_textview.visibility = View.VISIBLE
        (activity as GlobalNavigationActivity).toolbarLayout.ivlogo.visibility = View.GONE
    }


    companion object{

        fun newInstance() = HomeFragment()
    }

}