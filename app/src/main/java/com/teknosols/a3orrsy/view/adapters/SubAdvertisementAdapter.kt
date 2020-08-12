package com.teknosols.a3orrsy.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import com.bumptech.glide.Glide
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.SubCategory
import com.teknosols.a3orrsy.other.extensions.showMapDirection
import kotlinx.android.synthetic.main.fragment_current_advertisement.view.*
import kotlinx.android.synthetic.main.sub_categories_item_layout.view.*
import kotlinx.android.synthetic.main.sub_categories_item_layout.view.rating
import kotlinx.android.synthetic.main.sub_categories_item_layout.view.rlMap
import java.util.ArrayList

class SubAdvertisementAdapter(
    context: Context,
    private var mDatafiltered: ArrayList<SubCategory>,
    var listener: SubAdvertisementAdapter.ItemClickListener
) :
    RecyclerView.Adapter<SubAdvertisementAdapter.ViewHolder>() {

    private val mData: ArrayList<SubCategory>
    private val mInflater: LayoutInflater
    lateinit var mListener: SubAdvertisementAdapter.ItemClickListener
    lateinit var context: Context

    init {
        this.mInflater = LayoutInflater.from(context)
        this.mData = mDatafiltered
        this.mListener = listener
        this.context = context
    }

    val filter: Filter
        get() = object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    mDatafiltered = mData
                } else {
                    val filteredList = ArrayList<SubCategory>()
                    for (row in mData) {
                        if (row.p_name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }

                    mDatafiltered = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = mDatafiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                mDatafiltered = filterResults.values as ArrayList<SubCategory>
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.sub_categories_item_layout, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(mDatafiltered.get(position).category_id == 8){//if category is Cars then show price.
            holder.itemView.category_price.visibility = View.VISIBLE

            holder.category_name!!.text = mDatafiltered!![position].p_name
            holder.category_phone_no!!.text = mDatafiltered!![position].phone
            holder.category_location!!.text = mDatafiltered!![position].address
            holder.category_price!!.text = "${mDatafiltered!![position].price} (${context.getString(R.string.price_)})"
            holder.rating!!.rating = mDatafiltered!![position].rating

            if (!mDatafiltered.get(position).images.isNullOrEmpty()) {
                Glide.with(holder.category_image)
                    .load(
                        mDatafiltered
                            .get(position).images!![0].image
                    )
                    .into(holder.category_image)
            }
        }else{
            holder.category_name!!.text = mDatafiltered!![position].p_name
            holder.category_phone_no!!.text = mDatafiltered!![position].phone
            holder.category_location!!.text = mDatafiltered!![position].address
            holder.rating!!.rating = mDatafiltered!![position].rating

            if (!mDatafiltered.get(position).images.isNullOrEmpty()) {
                Glide.with(holder.category_image)
                    .load(
                        mDatafiltered
                            .get(position).images!![0].image
                    )
                    .into(holder.category_image)
            }
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
        var category_price = itemView.category_price
        var rating = itemView.rating

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
                        mDatafiltered[adapterPosition].lat.toDouble(),
                        mDatafiltered[adapterPosition].lng.toDouble()
                    )
                }
            }

        }
    }

    interface ItemClickListener {
        fun openDetail(advertisement: SubCategory)
    }
}