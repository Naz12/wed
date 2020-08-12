package com.teknosols.a3orrsy.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import kotlinx.android.synthetic.main.categories_item_layout.view.*

import java.util.ArrayList

class CategoryAdapter(context: Context, private var mDatafiltered: ArrayList<Category>,var listener: ItemClickListener) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val mData: ArrayList<Category>
    private val mInflater: LayoutInflater
    lateinit var mListener: ItemClickListener

    init {
        this.mInflater = LayoutInflater.from(context)
        this.mData = mDatafiltered
        this.mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.categories_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.category_name!!.text = mDatafiltered!![position].name

        Glide.with(holder.category_image)
            .load(mDatafiltered
            .get(position).image)
            .into(holder.category_image)
    }

    override fun getItemCount(): Int {
        return mDatafiltered!!.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var category_name = itemView.category_name
        var category_image = itemView.category_image

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mListener?.openDetail(mDatafiltered[adapterPosition])
        }
    }

    interface ItemClickListener {
        fun openDetail(category: Category)
    }
}