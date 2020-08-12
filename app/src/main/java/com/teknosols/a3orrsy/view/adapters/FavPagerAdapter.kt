package com.teknosols.a3orrsy.view.adapters

import com.teknosols.a3orrsy.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.google.android.libraries.places.internal.i
import com.teknosols.a3orrsy.datamodel.model.fan.Booking
import com.teknosols.a3orrsy.datamodel.model.fan.ProfileImage
import com.teknosols.a3orrsy.datamodel.model.fan.SubCategory
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.fragments.CurrentAdvertisementFragment
import com.teknosols.a3orrsy.view.fragments.CurrentBookingFragment

import java.util.ArrayList

class FavPagerAdapter(var context: Context, var favEvents : ArrayList<SubCategory>) :
    PagerAdapter() {
    lateinit var inflater: LayoutInflater
    lateinit var imageView: ImageView
    lateinit var tvCategoryName : TextView
    lateinit var tvCategoryPhoneNo : TextView
    lateinit var tvCategoryLocation : TextView


    override fun getCount(): Int {
        return favEvents.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {


        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView: View? = null
        itemView = inflater.inflate(R.layout.pager_item, container, false)


        imageView = itemView!!.findViewById(R.id.image)
        tvCategoryName = itemView!!.findViewById(R.id.tvCategoryName)
        tvCategoryPhoneNo = itemView!!.findViewById(R.id.tvCategoryPhoneNo)
        tvCategoryLocation = itemView!!.findViewById(R.id.tvCategoryLocation)

        Glide.with(context)
            .load(favEvents.get(position).images.get(0).image)
            .into(imageView)

        tvCategoryName.text = favEvents.get(position).name
        tvCategoryPhoneNo.text = favEvents.get(position).phone
        tvCategoryLocation.text = favEvents.get(position).address

        itemView.setOnClickListener(View.OnClickListener {

            if(favEvents.get(position).category_id == 1 ||
                favEvents.get(position).category_id == 2 ||
                favEvents.get(position).category_id == 3 ||
                favEvents.get(position).category_id == 5 ||
                favEvents.get(position).category_id == 11 ||
                favEvents.get(position).category_id == 14) { // if bookings then open booking fragment

                val currentBookingFragment =
                    CurrentBookingFragment.newInstance(
                        favEvents.get(position),
                        "Name",
                        favEvents.get(position).id.toString()
                    )
                (context as BaseActivity).replaceFragment(
                    currentBookingFragment, currentBookingFragment.getSimpleName(), true, false
                )
            }else{
                val currentAdvertisementFragment =
                    CurrentAdvertisementFragment.newInstance(
                        favEvents.get(position),
                        favEvents.get(position).category_id
                    )
                (context as BaseActivity).replaceFragment(
                    currentAdvertisementFragment, currentAdvertisementFragment.getSimpleName(), true, false
                )
            }
        })




        // add intro_itemso_items.xml to viewpager
        (container as ViewPager).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as RelativeLayout)
    }


}