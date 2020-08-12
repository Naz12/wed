package com.teknosols.a3orrsy.view.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_categories.*
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.CategoriesResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.UserResponse
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.gotoGlobalNavigationActivity
import com.teknosols.a3orrsy.other.extensions.obtainViewModel
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.adapters.CategoryAdapter
import com.teknosols.a3orrsy.view.decoration.ListSpacingDecoration
import com.teknosols.a3orrsy.viewmodel.CategoriesViewModel
import com.teknosols.a3orrsy.viewmodel.SignInViewModel
import kotlinx.android.synthetic.main.activity_global_navigation.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*


class CategoriesFragment : BaseFragment(), View.OnClickListener, CategoryAdapter.ItemClickListener {
    override fun openDetail(category: Category) {
        if(category.id == 7 || category.id == 8|| category.id == 10|| category.id == 9|| category.id == 12|| category.id == 13|| category.id == 15 || category.id == 19 || category.id == 18 || category.id == 20){
            val subAdvertisementFragment =
                SubAdvertisementFragment.newInstance(category)
            (activity as BaseActivity).replaceFragment(
                subAdvertisementFragment, subAdvertisementFragment.getSimpleName(), true, false
            )
        }else{
            val subCategoriesFragment = SubCategoriesFragment.newInstance(category)
            (activity as BaseActivity).replaceFragment(
                subCategoriesFragment,
                subCategoriesFragment.getSimpleName(),
                true,
                false
            )
        }

    }

    lateinit var viewModel: CategoriesViewModel
    lateinit var adapter: CategoryAdapter
    lateinit var categories: ArrayList<Category>


    override fun onClick(v: View?) {
        when (v) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_categories, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as GlobalNavigationActivity).toolbarLayout.header_textview.text = "Choose Your Event"

        viewModelHandler()
        swiperefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            viewModel.getCategories()
            swiperefresh.setRefreshing(false)
        })

    }

    fun viewModelHandler() {

        viewModel = obtainViewModel(CategoriesViewModel::class.java)
        viewModel.getCategories()

        viewModel.snackbarMessage.observe(viewLifecycleOwner, object : Observer<OneShotEvent<String>> {
            override fun onChanged(t: OneShotEvent<String>?) {
                var msg = t?.getContentIfNotHandled()
                if (!msg.isNullOrEmpty())
                    showAlertDialog(msg)
            }
        })

        viewModel.progressBar.observe(viewLifecycleOwner, object : Observer<OneShotEvent<Boolean>> {
            override fun onChanged(t: OneShotEvent<Boolean>?) {
                var show = t?.getContentIfNotHandled()
                if (show != null)
                    showProgressDialog(show)
            }
        })

        viewModel.categories.observe(viewLifecycleOwner, object : Observer<OneShotEvent<CategoriesResponse>> {
            override fun onChanged(t: OneShotEvent<CategoriesResponse>?) {
                var show = t?.getContentIfNotHandled()
                show?.let {
                    categories = it.data
                    setAdapter()
                }
            }

        })

    }

    fun setAdapter() {
        adapter = CategoryAdapter(context!!, categories, this)
        categories_recyclerview?.let {
            it.layoutManager = GridLayoutManager(context!!, 2) as LinearLayoutManager
            it.addItemDecoration(ListSpacingDecoration(resources.getDimensionPixelSize(R.dimen.recycler_view_spacing)))
            it.adapter = adapter
            if (it.itemDecorationCount > 1) {
                it.removeItemDecorationAt(0)
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoriesFragment()
    }


}
