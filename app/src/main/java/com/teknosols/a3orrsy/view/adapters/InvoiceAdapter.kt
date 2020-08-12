package com.teknosols.a3orrsy.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.datamodel.model.fan.DepositHistory
import com.teknosols.a3orrsy.other.extensions.parseDateIntoAppFormat
import kotlinx.android.synthetic.main.categories_item_layout.view.*
import kotlinx.android.synthetic.main.item_invoice.view.*

import java.util.ArrayList

class InvoiceAdapter(var context: Context, private var mDatafiltered: ArrayList<DepositHistory>) :
    RecyclerView.Adapter<InvoiceAdapter.ViewHolder>() {

    private val mData: ArrayList<DepositHistory>
    private val mInflater: LayoutInflater
    lateinit var mListener: ItemClickListener

    init {
        this.mInflater = LayoutInflater.from(context)
        this.mData = mDatafiltered
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_invoice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date!!.text = parseDateIntoAppFormat(mDatafiltered!![position].deposit_date!!)
        holder.deposit!!.text = mDatafiltered!![position].deposit_amount
        var pos: Int = position
        holder.transactionNo!!.text = "${context.getString(R.string.transaction)} ${++pos}"

    }

    override fun getItemCount(): Int {
        return mDatafiltered!!.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var date = itemView.tvDate
        var deposit = itemView.tvDeposit
        var transactionNo = itemView.tvTransaction

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
//            mListener?.openDetail(mDatafiltered[adapterPosition])
        }
    }

    interface ItemClickListener {
        fun openDetail(depositHistory: DepositHistory)
    }
}