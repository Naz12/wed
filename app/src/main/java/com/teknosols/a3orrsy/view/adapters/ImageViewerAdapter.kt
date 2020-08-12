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
import android.widget.LinearLayout

import com.bumptech.glide.Glide
import com.teknosols.a3orrsy.datamodel.model.fan.ProfileImage

import java.util.ArrayList

class ImageViewerAdapter( var context: Context,  var images: ArrayList<ProfileImage>) :
    PagerAdapter() {
    lateinit var inflater: LayoutInflater
    lateinit var imageView: ImageView


    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {


        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView: View? = null
        itemView = inflater.inflate(R.layout.item_image_viewer, container, false)

        imageView = itemView!!.findViewById(R.id.ivComplainImage)

        Glide.with(context)
            .load(images[position].image)
            .into(imageView)


        // add intro_itemso_items.xml to viewpager
        (container as ViewPager).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as LinearLayout)
    }


}

