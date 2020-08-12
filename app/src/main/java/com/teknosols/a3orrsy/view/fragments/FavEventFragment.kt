package com.teknosols.a3orrsy.view.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.SubCategory
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.SubCategoriesResponse
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.obtainViewModel
import com.teknosols.a3orrsy.other.extensions.showMapDirection
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.adapters.FavPagerAdapter
import com.teknosols.a3orrsy.viewmodel.FavEventsViewModel
import kotlinx.android.synthetic.main.fragment_fav_event.*
import java.util.*
import kotlin.collections.ArrayList

class FavEventFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v) {
            llFavEvent -> {
                if(checkBookingOrAdvertisement(favEvents.get(0).category_id).equals("booking")) { // if bookings then open booking fragment

                    val currentBookingFragment =
                        CurrentBookingFragment.newInstance(
                            favEvents.get(0),
                            "Name",
                            favEvents.get(0).id.toString()
                        )
                    (context as GlobalNavigationActivity).replaceFragment(
                        currentBookingFragment, currentBookingFragment.getSimpleName(), true, false
                    )
                }else{
                    val currentAdvertisementFragment =
                        CurrentAdvertisementFragment.newInstance(
                            favEvents.get(0),
                            favEvents.get(0).category_id
                        )
                    (context as GlobalNavigationActivity).replaceFragment(
                        currentAdvertisementFragment, currentAdvertisementFragment.getSimpleName(), true, false
                    )
                }
            }
            ivImageInCard -> {
                showMapDirection(
                    context!!,
                    favEvents.get(0).lat.toDouble(),
                    favEvents.get(0).lng.toDouble()
                )
            }
        }
    }

    lateinit var adapter: PagerAdapter
    lateinit var viewModel: FavEventsViewModel
    var favEvents: ArrayList<SubCategory> = ArrayList()

//    var images : ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        for (i in 0..3){
//            images.add(R.drawable.weeding_hall)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_fav_event, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        llFavEvent.setOnClickListener(this)
        ivImageInCard.setOnClickListener(this)
        viewModelHandler()

        srFavFragment.setOnRefreshListener {
            viewModel.getAllFavEvents()
            srFavFragment.isRefreshing = false
        }

    }


    private fun viewModelHandler() {
        viewModel = obtainViewModel(FavEventsViewModel::class.java)

        viewModel.getAllFavEvents()

        viewModel.snackbarMessage.observe(
            viewLifecycleOwner,
            object : Observer<OneShotEvent<String>> {
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

        viewModel.allBookingsResponse.observe(
            viewLifecycleOwner,
            object : Observer<OneShotEvent<SubCategoriesResponse>> {
                override fun onChanged(t: OneShotEvent<SubCategoriesResponse>?) {
                    var show = t?.getContentIfNotHandled()
                    show?.let {
                        favEvents = it.data
                        setUIAccordingly()
                        setAdapter()
                    }
                }
            })
    }

    private fun setUIAccordingly() {

        if (favEvents.size != 0) {

            if (checkBookingOrAdvertisement(favEvents.get(0).category_id).equals("booking")) {

                if (favEvents.get(0).category_id == 1 || favEvents.get(0).category_id == 2) {//if category is Hall or Club then show price and hall size
                    tvCategoryPriceCard.visibility = View.VISIBLE
                    tvCategorySizeCard.visibility = View.VISIBLE
                    tvCategoryPriceCard.text =
                        "${favEvents.get(0).price} (${context!!.getString(R.string.price_)})"
                    tvCategorySizeCard.text =
                        "${favEvents.get(0).size} (${context!!.getString(R.string.size)})"

                    setUI()

                } else if (favEvents.get(0).category_id == 5) {//if category is Singer then show price only
                    tvCategoryPriceCard.visibility = View.VISIBLE
                    tvCategoryPriceCard.text =
                        "${favEvents.get(0).price} (${context!!.getString(R.string.price_)})"

                    setUI()

                } else {
                    setUI()
                }

            } else {

                if (favEvents.get(0).category_id == 8) {// if add is Cars then show price only
                    tvCategoryPriceCard.visibility = View.VISIBLE
                    tvCategoryPriceCard.text =
                        "${favEvents.get(0).price} (${context!!.getString(R.string.price_)})"

                    setUI()

                }else{
                    setUI()
                }
            }
        }

    }

    private fun setUI() {
        if (checkBookingOrAdvertisement(favEvents.get(0).category_id).equals("booking")){

            tvCategoryNameCard.text = favEvents.get(0).name
            tvCategoryLocationCard.text = favEvents.get(0).address

        }else{

            tvCategoryNameCard.text = favEvents.get(0).p_name
            tvCategoryLocationCard.text = favEvents.get(0).destination

        }
        tvCategoryPhoneNoCard.text = favEvents.get(0).phone
        Glide.with(context!!)
            .load(favEvents.get(0).images.get(0))
            .into(ivFavEvent)

    }

    private fun setAdapter() {
        val subArray : ArrayList<SubCategory> = ArrayList(favEvents.subList(1 , favEvents.size))
        adapter = FavPagerAdapter(context!!, subArray)
        vpFavHalls.adapter = adapter
        indicator.setViewPager(vpFavHalls)
    }

}