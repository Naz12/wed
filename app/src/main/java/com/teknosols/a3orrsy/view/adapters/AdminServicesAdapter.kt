package com.teknosols.a3orrsy.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.*
import kotlinx.android.synthetic.main.item_service_booking.view.*

class AdminServicesAdapter(
    context: Context,
    private var mDatafiltered: Booking,
    var listener: AdminServicesAdapter.ItemClickListener
) :
    RecyclerView.Adapter<AdminServicesAdapter.ViewHolder>() {

    private val mData: Booking
    private val mInflater: LayoutInflater
    lateinit var mListener: AdminServicesAdapter.ItemClickListener
    lateinit var context: Context

    init {
        this.mInflater = LayoutInflater.from(context)
        this.mData = mDatafiltered
        this.mListener = listener
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_service_booking , parent , false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var totalPrice : Double = 0.0
        if( !mDatafiltered!!.services[position].bs_price.isNullOrEmpty() && !mDatafiltered!!.services[position].bs_number.isNullOrEmpty()){
            totalPrice = mDatafiltered!!.services[position].bs_price!!.toDouble() * mDatafiltered!!.services[position].bs_number!!.toDouble()
        }
        holder.name!!.text = mDatafiltered.services[position].bs_name
        holder.detail!!.text = mDatafiltered!!.services[position].bs_detail
        holder.price!!.text = "${mDatafiltered!!.services[position].bs_price} /-"
        holder.quantity!!.text = mDatafiltered!!.services[position].bs_number
        holder.totalPrice!!.text = "$totalPrice"
    }

    override fun getItemCount(): Int {
        return mDatafiltered!!.services.size
    }


    inner class ViewHolder internal constructor(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var name = itemView.tvServiceName
        var detail = itemView.tvServiceDetail
        var price = itemView.tvServicePrice
        var quantity = itemView.tvServiceQuantity
        var totalPrice = itemView.tvTotalPrice

        init {
            itemView.rlServiceDetail.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view) {
                itemView.rlServiceDetail ->{
                    mListener?.openDetail(mDatafiltered, mDatafiltered.services[adapterPosition],"service_detail")

                }
            }

        }
    }

    interface ItemClickListener {
        fun openDetail(booking: Booking, service: BookingServices, status: String)
    }
}