package com.teknosols.a3orrsy.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.smarteist.autoimageslider.DefaultSliderView
import com.smarteist.autoimageslider.IndicatorAnimations
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.datamodel.model.fan.SubCategory
import com.teknosols.a3orrsy.other.extensions.showMapDirection
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.adapters.ServicesAdapter
import kotlinx.android.synthetic.main.fragment_current_advertisement.*
import kotlinx.android.synthetic.main.fragment_current_advertisement.imageSlider
import kotlinx.android.synthetic.main.fragment_current_advertisement.rating
import kotlinx.android.synthetic.main.fragment_current_advertisement.rlMap
import kotlinx.android.synthetic.main.fragment_current_booking.*

class CurrentAdvertisementFragment : BaseFragment(),View.OnClickListener{
    override fun onClick(v: View?) {
        when (v) {
            rlMap -> {
                showMapDirection(context!!, advertisement.lat.toDouble() , advertisement.lng.toDouble())
            }
        }
    }

    var categoryId: Int = 0
    var advertisement = SubCategory()
//    lateinit var adapter: ServicesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            this.advertisement = it!!.getSerializable("advertisement") as SubCategory
            this.categoryId = it!!.getInt("categoryId") as Int
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_current_advertisement,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUIAccordingEvents()
        rlMap.setOnClickListener(this)

    }

    private fun setUIAccordingEvents() {
        if(categoryId == 8){
            llOwnerName.visibility = View.VISIBLE
            rlServices.visibility = View.GONE
            setCommonDataUI()
            setSliderViews()
            tvAddDetails.text = advertisement.details
            tvNameOfOwner.text = advertisement.owner_Name
            tvAddPrice.text = "${advertisement.price} /-"
        }else if(categoryId == 13){
            llDestination.visibility = View.VISIBLE
            setCommonDataUI()
            setSliderViews()
            tvAddDetails.text = advertisement.details
            tvAddDestination.text = advertisement.destination
            tvAddPrice.text = "${advertisement.price} per day"
        }else if(categoryId == 7){
            llPrice.visibility = View.GONE
            rlServices.visibility = View.GONE
            setCommonDataUI()
            setSliderViews()
            tvAddDetails.text = advertisement.details
        }else if(categoryId == 10){
            rlServices.visibility = View.GONE
            setCommonDataUI()
            setSliderViews()
            tvAddPrice.text = "${advertisement.price} /-"
            tvAddDetails.text = advertisement.details

        }else{
            setCommonDataUI()
            setSliderViews()
            tvAddPrice.text = "${advertisement.price} /-"
            tvAddDetails.text = advertisement.details
            rlServices.visibility = View.VISIBLE
        }
    }

    private fun setCommonDataUI() {
        tvCompanyName.text = advertisement.p_name
        tvAddLocation.text = advertisement.address
        tvAddPhoneNo.text = advertisement.phone
        rating.rating = advertisement.rating
    }

    private fun setSliderViews() {
        for (i in 0 until advertisement.images!!.size) {
            val sliderView = DefaultSliderView(context)

            sliderView.imageUrl = advertisement.images!!.get(i).image

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY)
//            sliderView.description = "setDescription " + (i + 1)
            //at last add this view in your layout :
            imageSlider.addSliderView(sliderView)
        }

        imageSlider.setIndicatorAnimation(IndicatorAnimations.THIN_WORM) //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setScrollTimeInSec(2) //set scroll delay in seconds :
    }

    companion object{

        fun newInstance(advertisement: SubCategory , categoryId: Int) : CurrentAdvertisementFragment{
            var fragment = CurrentAdvertisementFragment()
            var bundle = Bundle()
            bundle.putSerializable("advertisement" , advertisement)
            bundle.putSerializable("categoryId" , categoryId)
            fragment.arguments = bundle

            return fragment
        }

    }
}