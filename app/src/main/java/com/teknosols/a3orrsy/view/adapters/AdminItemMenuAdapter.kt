package com.teknosols.a3orrsy.view.adapters

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.other.extensions.gotoAdminActivity
import com.teknosols.a3orrsy.other.extensions.shareAppLink
import com.teknosols.a3orrsy.view.activites.AdminActivity
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.activites.LandingActivity
import com.teknosols.a3orrsy.view.fragments.AdminHomeFragment
import com.teknosols.a3orrsy.view.fragments.EditProfileFragment
import kotlinx.android.synthetic.main.activity_global_navigation.*
import kotlinx.android.synthetic.main.nev_items_main.view.*

class AdminItemMenuAdapter(context: Context) :
    RecyclerView.Adapter<AdminItemMenuAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater
    var titles = arrayOf(context.getString(R.string.change_language))
    var images = intArrayOf(R.drawable.change_language)
    var context: Context

    init {
        this.mInflater = LayoutInflater.from(context)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.nev_items_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.image)
            .load(images[position])
            .into(holder.image)

        holder.name.setText(titles[position])
        holder.itemView.setOnClickListener(View.OnClickListener {
            when (holder.getAdapterPosition()) {
                //Change Language
                0 -> {
                    (context as AdminActivity).drawer_layout.closeDrawers()
                    (context as AdminActivity).showChangeLanguageDialog()
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return titles!!.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var name = itemView.tvTitle
        var image = itemView.itemImage

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {

        }
    }

    interface ItemClickListener {
        fun openDetail(category: Category)
    }
}