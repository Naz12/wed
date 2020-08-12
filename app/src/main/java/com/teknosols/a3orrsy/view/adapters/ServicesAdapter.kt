package com.teknosols.a3orrsy.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.datamodel.model.fan.Service
import com.teknosols.a3orrsy.datamodel.model.fan.SubCategory
import com.teknosols.a3orrsy.other.extensions.showMapDirection
import kotlinx.android.synthetic.main.item_sevice.view.*
import java.util.ArrayList

class ServicesAdapter(
    context: Context,
    private var mDatafiltered: SubCategory,
    var listener: ServicesAdapter.ItemClickListener
) :
    RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    private val mData: SubCategory
    private val mInflater: LayoutInflater
    lateinit var mListener: ServicesAdapter.ItemClickListener
    lateinit var context: Context

    init {
        this.mInflater = LayoutInflater.from(context)
        this.mData = mDatafiltered
        this.mListener = listener
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_sevice, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name!!.text = mDatafiltered!!.services[position].service_name
        holder.detail!!.text = mDatafiltered!!.services[position].service_detail
        holder.price!!.text = "${mDatafiltered!!.services[position].service_price} /-"
        holder.quantity!!.text = mDatafiltered!!.services[position].service_number
        mDatafiltered!!.services[position].my_quantity = mDatafiltered!!.services[position].service_number

        if(mDatafiltered.services[position].service_kind == "mandatory"){
            Glide.with(context)
                .load(R.drawable.check_box_checked)
                .into(holder.checkBox)
            mDatafiltered.services[position].my_status = true
        }else {
            Glide.with(context)
                .load(R.drawable.check_box_unchecked)
                .into(holder.checkBox)
            mDatafiltered.services[position].my_status = false
        }

        if(position == mDatafiltered!!.services.size-1){
            mListener?.openDetail(mDatafiltered, mDatafiltered.services[position],"")
        }
    }

    override fun getItemCount(): Int {
        return mDatafiltered!!.services.size
    }

    inner class ViewHolder internal constructor(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var checkBox = itemView.ivCheckbox
        var name = itemView.tvName
        var detail = itemView.tvDetail
        var price = itemView.tvPrice
        var quantity = itemView.tvQuantity

        init {
            itemView.rlCheckBtn.setOnClickListener(this)
            itemView.rlDetail.setOnClickListener(this)
            itemView.tvPlus.setOnClickListener(this)
            itemView.tvMinus.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view) {
                itemView.rlCheckBtn -> {
                    if(mDatafiltered.services[adapterPosition].service_kind != "mandatory" && !mDatafiltered.services[adapterPosition].my_status){
                        Glide.with(context)
                            .load(R.drawable.check_box_checked)
                            .into(itemView.ivCheckbox)
                        mDatafiltered.services[adapterPosition].my_status = true
                    }else if(mDatafiltered.services[adapterPosition].service_kind != "mandatory" && mDatafiltered.services[adapterPosition].my_status){
                        Glide.with(context)
                            .load(R.drawable.check_box_unchecked)
                            .into(itemView.ivCheckbox)
                        mDatafiltered.services[adapterPosition].my_status = false
                    }
                    else if(mDatafiltered.services[adapterPosition].service_kind == "mandatory" && mDatafiltered.services[adapterPosition].my_status){
                        Toast.makeText(context, "This Is Mandatory", Toast.LENGTH_SHORT).show()
                    }
                   mListener?.openDetail(mDatafiltered, mDatafiltered.services[adapterPosition],"")
                }
                itemView.rlDetail -> {
                    mListener?.openDetail(mDatafiltered, mDatafiltered.services[adapterPosition],"service_detail")
                }
                itemView.tvPlus -> {
                    var value = mDatafiltered.services[adapterPosition].my_quantity!!.toLong()
                    value++
                    mDatafiltered.services[adapterPosition].my_quantity = value.toString()
                    itemView.tvQuantity!!.text = value.toString()
                    mListener?.openDetail(mDatafiltered, mDatafiltered.services[adapterPosition],"")
                }
                itemView.tvMinus -> {
                    if(itemView.tvQuantity.text.toString().toLong() > mDatafiltered.services[adapterPosition].service_number!!.toLong()){
                        var value = mDatafiltered.services[adapterPosition].my_quantity!!.toLong()
                        value--
                        mDatafiltered.services[adapterPosition].my_quantity = value.toString()
                        itemView.tvQuantity!!.text = value.toString()
                        mListener?.openDetail(mDatafiltered, mDatafiltered.services[adapterPosition],"")
                    }
                }
            }

        }
    }

    interface ItemClickListener {
        fun openDetail(subCategory: SubCategory, service: Service, status: String)
    }
}