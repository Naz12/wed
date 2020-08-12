package com.teknosols.a3orrsy.view.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.datamodel.model.fan.SubCategory
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.SubCategoriesResponse
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.obtainViewModel
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.adapters.SubAdvertisementAdapter
import com.teknosols.a3orrsy.view.decoration.ListSpacingDecoration
import com.teknosols.a3orrsy.viewmodel.SubAdvertisementViewModel
import kotlinx.android.synthetic.main.activity_global_navigation.*
import kotlinx.android.synthetic.main.fragment_sub_categories.*
import kotlinx.android.synthetic.main.item_search.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class SubAdvertisementFragment : BaseFragment(), SubAdvertisementAdapter.ItemClickListener,
    View.OnClickListener {

    override fun onClick(v: View?) {

        when (v) {
            llFilter -> {
                openDialogue()
            }
        }
    }

    override fun openDetail(advertisement: SubCategory) {
        val currentAdvertisementFragment =
            CurrentAdvertisementFragment.newInstance(advertisement, advertisement.category_id)
        (activity as BaseActivity).replaceFragment(
            currentAdvertisementFragment, currentAdvertisementFragment.getSimpleName(), true, false
        )
    }


    lateinit var category: Category
    lateinit var adapter: SubAdvertisementAdapter
    var advertisement: ArrayList<SubCategory> = ArrayList()
    val images: ArrayList<Int> = ArrayList()
    var selectedOrder: String? = null
    var selectedFilter: String? = null
    var dialog_alert: AlertDialog? = null
    lateinit var viewModel: SubAdvertisementViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = arguments.let {
            it!!.getSerializable("category") as Category
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_sub_categories, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as GlobalNavigationActivity).toolbarLayout.header_textview.text = category.name
        rbDateAndTime.text = getString(R.string.all)
        etSearch.visibility = View.GONE
        llDateAndTime.visibility = View.GONE
        llFilter.setOnClickListener(this)
        viewModelHandler()

        rgSearched.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.rbName -> {
                    etSearch.setText("")
                    llParent.visibility = View.GONE
                    etSearch.visibility = View.VISIBLE
                }

                R.id.rbDateAndTime -> {
                    etSearch.visibility = View.GONE
                    setAdapter()
                }
            }
        })
        searchHandler()

    }

    private fun viewModelHandler() {

        viewModel = obtainViewModel(SubAdvertisementViewModel::class.java)

        viewModel.getAllAdvertisement(category.id.toString())

        viewModel.snackbarMessage.observe(
            viewLifecycleOwner,
            object : Observer<OneShotEvent<String>> {
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

        viewModel.advertisement.observe(
            viewLifecycleOwner,
            object : Observer<OneShotEvent<SubCategoriesResponse>> {
                override fun onChanged(t: OneShotEvent<SubCategoriesResponse>?) {

                    var show = t?.getContentIfNotHandled()
                    show?.let {
                        advertisement = it.data
                        setAdapter()
                    }
                }

            })
    }

    override fun onResume() {
        super.onResume()
        viewModelHandler()
        rbDateAndTime.isChecked = true
    }

    private fun setAdapter() {
        adapter = SubAdvertisementAdapter(context!!, advertisement, this)
        sub_categories_recyclerview?.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            it.addItemDecoration(ListSpacingDecoration(resources.getDimensionPixelSize(R.dimen.recycler_view_grid_spacing)))
            it.adapter = adapter
            if (it.itemDecorationCount > 1) {
                it.removeItemDecorationAt(0)
            }
        }
        llFilter.visibility = View.VISIBLE
        llParent.visibility = View.VISIBLE
    }

    fun searchHandler() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: kotlin.Int,
                count: kotlin.Int,
                after: kotlin.Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: kotlin.Int,
                before: kotlin.Int,
                count: kotlin.Int
            ) {
                searchCheck(count)
                if (advertisement.size > 0)
                    adapter.filter.filter(etSearch.text.toString())
            }
        })
    }

    fun searchCheck(count: kotlin.Int) {
        if (count == 0){
            llFilter.visibility = View.GONE
            llParent.visibility = View.GONE
        }

        else{
            llFilter.visibility = View.VISIBLE
            llParent.visibility = View.VISIBLE
        }

    }

    private fun openDialogue(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(context!!)

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.filter_dialogue, null)
        dialogBuilder.setView(dialogView)

        var name: RelativeLayout = dialogView.findViewById(R.id.tvFilterName1)
        var price: RelativeLayout = dialogView.findViewById(R.id.tvFilterName2)
        var size: RelativeLayout = dialogView.findViewById(R.id.tvFilterName3)
        var checkBoxName: ImageView = dialogView.findViewById(R.id.ivCheck1)
        var checkBoxPrice: ImageView = dialogView.findViewById(R.id.ivCheck2)
        var checkBoxSize: ImageView = dialogView.findViewById(R.id.ivCheck3)

        if (category.id == 8) {// if Cars then show sort by price
            price.visibility = View.VISIBLE
        }

        name.setOnClickListener {
            dialog_alert!!.dismiss()
            val mBuilder = android.app.AlertDialog.Builder(context)
            mBuilder.setSingleChoiceItems(resources.getStringArray(R.array.asc_dsc),
                checkSelected(),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    when (i) {
                        0 -> {
                            //ascending order
                            selectedFilter = "name"
                            selectedOrder = "asc"
                            Collections.sort(advertisement, object : Comparator<SubCategory> {
                                override fun compare(o1: SubCategory?, o2: SubCategory?): Int {
                                    return o1!!.p_name.compareTo(o2!!.p_name, true)
                                }
                            })
                            setAdapter()
                        }
                        1 -> {
                            //descending order
                            selectedFilter = "name"
                            selectedOrder = "dsc"
                            Collections.sort(advertisement, object : Comparator<SubCategory> {
                                override fun compare(o1: SubCategory?, o2: SubCategory?): Int {
                                    return o2!!.p_name.compareTo(o1!!.p_name, true)
                                }
                            })
                            setAdapter()
                        }

                    }
                    dialogInterface.dismiss()
                })

            val mDialog = mBuilder.create()
            mDialog.show()
        }

        price.setOnClickListener {
            dialog_alert!!.dismiss()
            val mBuilder = android.app.AlertDialog.Builder(context)
            mBuilder.setTitle(getString(R.string.order))

            mBuilder.setSingleChoiceItems(resources.getStringArray(R.array.asc_dsc),
                checkSelected(),
                DialogInterface.OnClickListener { dialogInterface, i ->

                    when (i) {
                        0 -> {
                            //ascending order
                            selectedFilter = "price"
                            selectedOrder = "asc"
                            Collections.sort(advertisement, object : Comparator<SubCategory> {
                                override fun compare(o1: SubCategory?, o2: SubCategory?): Int {
                                    if (o1!!.price.toInt() < o2!!.price.toInt())
                                        return -1
                                    else {
                                        if (o1!!.price.toInt() > o2.price.toInt())
                                            return 1
                                        else
                                            return 0
                                    }
                                }
                            })
                            setAdapter()
                        }
                        1 -> {
                            //descending order
                            selectedFilter = "price"
                            selectedOrder = "dsc"
                            Collections.sort(advertisement, object : Comparator<SubCategory> {
                                override fun compare(o1: SubCategory?, o2: SubCategory?): Int {
                                    if (o1!!.price.toInt() > o2!!.price.toInt())
                                        return -1
                                    else {
                                        if (o1!!.price.toInt() < o2.price.toInt())
                                            return 1
                                        else
                                            return 0
                                    }
                                }
                            })
                            setAdapter()
                        }
                    }
                    dialogInterface.dismiss()
                })

            val mDialog = mBuilder.create()
            mDialog.show()

        }

        if (!selectedFilter.isNullOrEmpty() && selectedFilter.equals("price") && !selectedOrder.isNullOrEmpty()) {
            checkBoxName.visibility = View.GONE
            checkBoxPrice.visibility = View.VISIBLE
            checkBoxSize.visibility = View.GONE
        } else if (!selectedFilter.isNullOrEmpty() && selectedFilter.equals("name")  && !selectedOrder.isNullOrEmpty()) {
            checkBoxName.visibility = View.VISIBLE
            checkBoxPrice.visibility = View.GONE
            checkBoxSize.visibility = View.GONE
        }


        dialog_alert = dialogBuilder.create()
        dialog_alert?.show()
        return dialog_alert!!

    }

    private fun checkSelected(): Int {
        if (!selectedOrder.isNullOrEmpty() && selectedOrder.equals("asc"))
            return 0
        else if (!selectedOrder.isNullOrEmpty() && selectedOrder.equals("dsc"))
            return 1
        else
            return -1
    }


    companion object {

        fun newInstance(category: Category): SubAdvertisementFragment {
            val fragment = SubAdvertisementFragment()
            val bundle = Bundle()
            bundle.putSerializable("category", category)
            fragment.arguments = bundle
            return fragment
        }
    }

}