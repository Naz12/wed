package com.teknosols.a3orrsy.view.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Booking
import kotlinx.android.synthetic.main.notification_items_main.view.*

class UserNotificationMenuAdapter(
    context: Context,
    bookings: ArrayList<Booking>,
    var listener: ItemClickListener
) :
    RecyclerView.Adapter<UserNotificationMenuAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater
    var context: Context
    var bookings: ArrayList<Booking> = ArrayList()
    lateinit var mListener: ItemClickListener

    init {
        this.mInflater = LayoutInflater.from(context)
        this.context = context
        this.bookings = bookings
        this.mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.notification_items_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (bookings.get(position).notifications.get(0).is_seen == 0) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.unread_notifcation
                )
            )
        }
        if (position == itemCount - 1) {
            holder.line.visibility = View.GONE
        }
        Glide.with(holder.image)
            .load(bookings.get(position).event_details?.images?.get(0)?.image)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.image)

        holder.name.setText(bookings.get(position).event_details?.name)
        holder.describtion.setText(bookings.get(position).notifications.get(0).message)
        holder.duration.setText(bookings.get(position).notifications.get(0).created_at)

//        holder.itemView.setOnClickListener(View.OnClickListener {
//            (context as GlobalNavigationActivity).drawer_layout.closeDrawers()
//            val adminHomeFragment = AdminHomeFragment.newInstance()
//            (context as GlobalNavigationActivity).replaceFragment(adminHomeFragment , adminHomeFragment.getSimpleName() , true , false)
//        })
    }

    override fun getItemCount(): Int {
        return bookings.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var name = itemView.tvEventName
        var image = itemView.ivEventPicture
        var describtion = itemView.tvEventDetail
        var duration = itemView.tvMonthsAgo
        var line = itemView.vLine

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mListener?.openDetail(bookings[adapterPosition])

        }
    }

    interface ItemClickListener {
        fun openDetail(booking: Booking)
    }
}