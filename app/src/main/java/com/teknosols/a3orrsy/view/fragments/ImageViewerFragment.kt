package com.teknosols.a3orrsy.view.fragments

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.ProfileImage
import com.teknosols.a3orrsy.view.adapters.ImageViewerAdapter
import kotlinx.android.synthetic.main.fragment_admin_home.viewpager
import java.util.ArrayList


class ImageViewerFragment : BaseFragment(), View.OnClickListener{

    lateinit var adapter: PagerAdapter
    var curretPage: Int = 0
    lateinit var images: ArrayList<ProfileImage>
     var selectedPosition: Int = 0

    override fun onClick(v: View?) {
        when(v){

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            this.images = it!!["images"] as ArrayList<ProfileImage>
            this.selectedPosition = it!!["selectedPosition"] as Int
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_image_viewer, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }


    fun setAdapter() {
        adapter = ImageViewerAdapter(context!!, images)
        viewpager.adapter = adapter
        viewpager.currentItem = selectedPosition
    }


    companion object {

        @JvmStatic
        fun newInstance(images:  ArrayList<ProfileImage>, selectedPosition: Int): ImageViewerFragment {
            var fragment = ImageViewerFragment()
            var bundle = Bundle()
            bundle.putSerializable("images", images)
            bundle.putInt("selectedPosition", selectedPosition)
            fragment.arguments = bundle
            return fragment
        }

    }


}
