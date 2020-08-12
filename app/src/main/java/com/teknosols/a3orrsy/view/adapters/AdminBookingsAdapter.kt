package com.teknosols.a3orrsy.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import com.bumptech.glide.Glide
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Booking
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.datamodel.model.fan.SubCategory
import com.teknosols.a3orrsy.other.extensions.showMapDirection
import kotlinx.android.synthetic.main.item_layout_admin_bookings.view.*
import java.util.ArrayList

class AdminBookingsAdapter(
    context: Context,
    private var mDatafiltered: ArrayList<Booking>,
    var listener: ItemClickListener
) :
    RecyclerView.Adapter<AdminBookingsAdapter.ViewHolder>() {

    private val mData: ArrayList<Booking>
    private val mInflater: LayoutInflater
    lateinit var mListener: ItemClickListener
    lateinit var context: Context

    init {
        this.mInflater = LayoutInflater.from(context)
        this.mData = mDatafiltered
        this.mListener = listener
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_layout_admin_bookings, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.category_name!!.text = mDatafiltered!![position].event_details!!.name
        holder.category_phone_no!!.text = mDatafiltered!![position].event_details!!.phone
        holder.category_location!!.text = mDatafiltered!![position].event_details!!.address
        holder.category_title!!.text = mDatafiltered!![position].category_details!!.name

        if (!mDatafiltered.get(position).event_details!!.images.isNullOrEmpty()) {
            Glide.with(holder.category_image)
                .load(
                    mDatafiltered
                        .get(position).event_details!!.images[0].image
                )
                .into(holder.category_image)
        }
    }

    override fun getItemCount(): Int {
        return mDatafiltered!!.size
    }

    inner class ViewHolder internal constructor(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var category_image = itemView.category_image
        var category_name = itemView.category_name
        var category_phone_no = itemView.category_phone_no
        var category_location = itemView.category_location
        var category_title = itemView.category_title

        init {
            itemView.llWholeItem.setOnClickListener(this)
            itemView.rlMap.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view) {
                itemView.llWholeItem -> {
                    mListener?.openDetail(mDatafiltered[adapterPosition])
                }
                itemView.rlMap -> {
                    showMapDirection(
                        context,
                        mDatafiltered[adapterPosition].event_details!!.lat.toDouble(),
                        mDatafiltered[adapterPosition].event_details!!.lng.toDouble()
                    )
                }
            }

        }
    }

    interface ItemClickListener {
        fun openDetail(booking: Booking)
    }
}