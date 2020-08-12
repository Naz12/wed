package com.teknosols.a3orrsy.view.fragments

import android.annotation.TargetApi
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.datamodel.model.fan.SubCategory
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.CitiesResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.SubCategoriesResponse
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.obtainViewModel
import com.teknosols.a3orrsy.other.util.DatePickerF
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.adapters.SubCategoryAdapter
import com.teknosols.a3orrsy.view.decoration.ListSpacingDecoration
import com.teknosols.a3orrsy.viewmodel.SubCategoryViewModel
import kotlinx.android.synthetic.main.activity_global_navigation.*
import kotlinx.android.synthetic.main.filter_dialogue.*
import kotlinx.android.synthetic.main.fragment_sub_categories.*
import kotlinx.android.synthetic.main.item_search.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SubCategoriesFragment : BaseFragment(), View.OnClickListener,
    SubCategoryAdapter.ItemClickListener {
    override fun openDetail(subCategory: SubCategory) {
        if (searchedBy == "Name") {
            val currentBookingFragment =
                CurrentBookingFragment.newInstance(subCategory, searchedBy, category.id.toString())
            (activity as BaseActivity).replaceFragment(
                currentBookingFragment, currentBookingFragment.getSimpleName(), true, false
            )

        } else {
            val currentBookingFragment = CurrentBookingFragment.newInstance(
                subCategory, searchedBy, startdate.text.toString(),
                startdate.text.toString(), spTime.selectedItem.toString(),
                tvNoOfDays.text.toString(), category.id.toString()
            )
            (activity as BaseActivity).replaceFragment(
                currentBookingFragment,
                currentBookingFragment.getSimpleName(),
                true,
                false
            )
        }
    }

    lateinit var adapter: SubCategoryAdapter
    var subCategories: ArrayList<SubCategory> = ArrayList()
    lateinit var category: Category
    var dialog_alert: AlertDialog? = null
    var selectedFilter : String? = null
    var selectedOrder : String? = "none"


    lateinit var cityAdapter: ArrayAdapter<String>

    var searchedBy = "Date & Time"

    var simpleDateFormat = SimpleDateFormat("dd-M-yyyy")

    lateinit var viewModel: SubCategoryViewModel

    override fun onClick(v: View?) {
        when (v) {
            startdate -> {
                val newDatePicker = DatePickerF()
                (newDatePicker as DatePickerF).setDateSetListner(object :
                    DatePickerF.DateSetListner {
                    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
                        var xMonth = month
                        xMonth++
                        startdate.setText(
                            Integer.toString(day) + "-" + Integer.toString(xMonth) + "-" + Integer.toString(
                                year
                            )
                        )
                        datesDifferenceHandler()
                    }
                })
                newDatePicker.show(fragmentManager, "datePicker")

            }

            enddate -> {
                val newDatePicker = DatePickerF()
                (newDatePicker as DatePickerF).setDateSetListner(object :
                    DatePickerF.DateSetListner {
                    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
                        var xMonth = month
                        xMonth++
                        enddate.setText(
                            Integer.toString(day) + "-" + Integer.toString(xMonth) + "-" + Integer.toString(
                                year
                            )
                        )
                        datesDifferenceHandler()
                    }
                })
                newDatePicker.show(fragmentManager, "datePicker")

            }

            llFilter -> {
                openDialogue()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            category = it?.get("category") as Category
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

        viewModelHandler()
        setSpinnersData()
        searchHandler()
        setOnClickListeners()

        swiperefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            refreshData()
            swiperefresh.setRefreshing(false)
        })


    }

    fun refreshData() {

    }

    fun setAdapter() {
        adapter = SubCategoryAdapter(context!!, subCategories, this)
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


    fun viewModelHandler() {
        viewModel = obtainViewModel(SubCategoryViewModel::class.java)

        viewModel.getCities()

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

        viewModel.subCategory.observe(
            viewLifecycleOwner,
            object : Observer<OneShotEvent<SubCategoriesResponse>> {
                override fun onChanged(t: OneShotEvent<SubCategoriesResponse>?) {
                    var show = t?.getContentIfNotHandled()
                    show?.let {
                        subCategories = ArrayList()
                        subCategories = it.data
                        setAdapter()
                        llFilter.visibility = View.GONE
                        llParent.visibility = View.GONE
                    }
                }
            })

        viewModel.availableSubCategory.observe(
            viewLifecycleOwner,
            object : Observer<OneShotEvent<SubCategoriesResponse>> {
                override fun onChanged(t: OneShotEvent<SubCategoriesResponse>?) {
                    var show = t?.getContentIfNotHandled()
                    show?.let {
                        subCategories = ArrayList()
                        subCategories = it.data
                        setAdapter()
                        llFilter.visibility = View.VISIBLE
                        llParent.visibility = View.VISIBLE
                    }
                }
            })

        viewModel.citiesResponse.observe(
            viewLifecycleOwner,
            object : Observer<OneShotEvent<CitiesResponse>> {
                override fun onChanged(t: OneShotEvent<CitiesResponse>?) {
                    var show = t?.getContentIfNotHandled()
                    show?.let {

                        var cityOptions = ArrayList<String>()
                        for (x in 0 until it.data.size) {
                            cityOptions.add(it.data.get(x).name!!)
                        }

                        cityAdapter =
                            ArrayAdapter<String>(context, R.layout.spinner_item, cityOptions)
                        cityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                        spCities.setAdapter(cityAdapter)
                    }
                }
            })
    }

    fun setSpinnersData() {
        // Time Spinner
        var timeOptions = context!!.resources.getStringArray(R.array.time_options)
        val timeAdapter = ArrayAdapter<String>(context, R.layout.spinner_item, timeOptions)
        timeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spTime.setAdapter(timeAdapter)


        spTime?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                datesDifferenceHandler()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        spCities?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                datesDifferenceHandler()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })


        etSearch.visibility = View.GONE
        llDateAndTime.visibility = View.VISIBLE
        llFilter.visibility = View.GONE
        llParent.visibility = View.GONE
        searchedBy = "Date & Time"
        datesDifferenceHandler()

        rgSearched.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.rbName -> {
                    etSearch.visibility = View.VISIBLE
                    llDateAndTime.visibility = View.GONE
                    searchedBy = "Name"
                    viewModel.getSubCategories(category.id.toString())
                }
                R.id.rbDateAndTime -> {
                    etSearch.visibility = View.GONE
                    llDateAndTime.visibility = View.VISIBLE
                    llFilter.visibility = View.GONE
                    llParent.visibility = View.GONE
                    searchedBy = "Date & Time"
                    datesDifferenceHandler()
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModelHandler()
        rbDateAndTime.isChecked = true
    }

    private fun commaSaperate(targetString: String): List<String> {
        val listOf = targetString.split(',').map { it.trim() }
        return listOf
    }

    fun searchHandler() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchCheck(count)
                if (subCategories.size > 0)
                    adapter.filter.filter(etSearch.text.toString())
            }
        })
    }

    fun searchCheck(count: Int) {
        if (count == 0){
            llFilter.visibility = View.GONE
            llParent.visibility = View.GONE
        }else{
            llFilter.visibility = View.VISIBLE
            llParent.visibility = View.VISIBLE
        }

    }

    fun setOnClickListeners() {
        startdate.setOnClickListener(this)
        enddate.setOnClickListener(this)
        llFilter.setOnClickListener(this)
    }

    fun dateAndTimeAPICall() {
        var time = spTime.selectedItem.toString()
        if (!startdate.text.isNullOrEmpty() && !startdate.text.isNullOrEmpty() && !time.isNullOrEmpty()) {
            viewModel.getAvailableSubCategories(
                category.id.toString(),
                startdate.text.toString(),
                startdate.text.toString(),
                time,
                spCities.selectedItem.toString()
            )
        }
    }


    fun datesDifferenceHandler() {
        try {

            if (!startdate.text.isNullOrEmpty() && !startdate.text.isNullOrEmpty()) {
                val date1 = simpleDateFormat.parse(startdate.text.toString())
                val date2 = simpleDateFormat.parse(startdate.text.toString())
                printDifference(date1, date2)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun printDifference(startDate: Date, endDate: Date) {
        //milliseconds
        var different = endDate.time - startDate.time

//        Log.i("MyDays","startDate : $startDate")
//        Log.i("MyDays","endDate : $endDate")
//        Log.i("MyDays","different : $different")

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli
        different = different % daysInMilli

        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli

        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli

        val elapsedSeconds = different / secondsInMilli

//        Log.i("MyDays",
//            "%d days, %d hours, %d minutes, %d seconds%n"+
//            "$elapsedDays $elapsedHours $elapsedMinutes $elapsedSeconds"
//        )

        var days: Int = elapsedDays.toInt()
        days++
        if (days > 0) {
            tvNoOfDays.text = days.toString()
            dateAndTimeAPICall()
        } else {
            llFilter.visibility = View.GONE
            llParent.visibility = View.GONE
            tvNoOfDays.text = "0"
            showAlertDialog(getString(R.string.invalid_dates))
        }
    }

    fun setUIAccordingEvents() {
        var categoryID = category.id
        if (categoryID == 1 || categoryID == 2 || categoryID == 5 || categoryID == 6 || categoryID == 2) {
            llParent.visibility == View.GONE
            line.visibility == View.VISIBLE
            rlSearchBy.visibility == View.VISIBLE
        } else {
            llParent.visibility == View.VISIBLE
            line.visibility == View.GONE
            rlSearchBy.visibility == View.GONE
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun openDialogue() : AlertDialog{

        val dialogBuilder = AlertDialog.Builder(context!!)

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.filter_dialogue, null)
        dialogBuilder.setView(dialogView)

        var name: RelativeLayout = dialogView.findViewById(R.id.tvFilterName1)
        var price: RelativeLayout = dialogView.findViewById(R.id.tvFilterName2)
        var size: RelativeLayout = dialogView.findViewById(R.id.tvFilterName3)
        var checkBoxName : ImageView = dialogView.findViewById(R.id.ivCheck1)
        var checkBoxPrice : ImageView = dialogView.findViewById(R.id.ivCheck2)
        var checkBoxSize : ImageView = dialogView.findViewById(R.id.ivCheck3)

        if(category.id == 1 || category.id == 2){// if halls and club show sort by price and size
            price.visibility = View.VISIBLE
            size.visibility = View.VISIBLE
        }else if (category.id == 5){// if singer then show sort by price only
            price.visibility = View.VISIBLE
        }

        name.setOnClickListener {
            dialog_alert!!.dismiss()
            val mBuilder = android.app.AlertDialog.Builder(context)
            mBuilder.setSingleChoiceItems(resources.getStringArray(R.array.asc_dsc), checkSelected(),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    when (i) {
                        0 -> {
                            //ascending order
                            selectedFilter = "name"
                            selectedOrder = "asc"
                            Collections.sort(subCategories, object : Comparator<SubCategory> {
                                override fun compare(o1: SubCategory?, o2: SubCategory?): Int {
                                    return o1!!.name.compareTo(o2!!.name, true)
                                }
                            })
                            setAdapter()
                        }
                        1 -> {
                            //descending order
                            selectedFilter = "name"
                            selectedOrder = "dsc"
                            Collections.sort(subCategories, object : Comparator<SubCategory> {
                                override fun compare(o1: SubCategory?, o2: SubCategory?): Int {
                                    return o2!!.name.compareTo(o1!!.name, true)
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

            mBuilder.setSingleChoiceItems(resources.getStringArray(R.array.asc_dsc), checkSelected(),
                DialogInterface.OnClickListener { dialogInterface, i ->

                    when (i) {
                        0 -> {
                            //ascending order
                            selectedFilter = "price"
                            selectedOrder = "asc"
                            Collections.sort(subCategories, object : Comparator<SubCategory> {
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
                            Collections.sort(subCategories, object : Comparator<SubCategory> {
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

        size.setOnClickListener {
            dialog_alert!!.dismiss()
            val mBuilder = android.app.AlertDialog.Builder(context)
            mBuilder.setTitle(getString(R.string.order))

            mBuilder.setSingleChoiceItems(resources.getStringArray(R.array.asc_dsc), checkSelected(),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    when (i) {
                        0 -> {
                            //ascending order
                            selectedFilter = "size"
                            selectedOrder = "asc"
                            Collections.sort(subCategories, object : Comparator<SubCategory> {
                                override fun compare(o1: SubCategory?, o2: SubCategory?): Int {
                                    if (o1!!.size.toInt() < o2!!.size.toInt())
                                        return -1
                                    else {
                                        if (o1!!.size.toInt() > o2.size.toInt())
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
                            selectedFilter = "size"
                            selectedOrder = "dsc"
                            Collections.sort(subCategories, object : Comparator<SubCategory> {
                                override fun compare(o1: SubCategory?, o2: SubCategory?): Int {
                                    if (o1!!.size.toInt() > o2!!.size.toInt())
                                        return -1
                                    else {
                                        if (o1!!.size.toInt() < o2.size.toInt())
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

        if(!selectedFilter.isNullOrEmpty() && selectedFilter.equals("size")){
            checkBoxName.visibility = View.GONE
            checkBoxPrice.visibility = View.GONE
            checkBoxSize.visibility = View.VISIBLE
        }else if(!selectedFilter.isNullOrEmpty() && selectedFilter.equals("price")){
            checkBoxName.visibility = View.GONE
            checkBoxPrice.visibility = View.VISIBLE
            checkBoxSize.visibility = View.GONE
        }else if(!selectedFilter.isNullOrEmpty() && selectedFilter.equals("name")){
            checkBoxName.visibility = View.VISIBLE
            checkBoxPrice.visibility = View.GONE
            checkBoxSize.visibility = View.GONE
        }


        dialog_alert = dialogBuilder.create()
        dialog_alert?.show()
        return dialog_alert!!

    }

    private fun checkSelected(): Int {
        if(!selectedOrder.isNullOrEmpty() && selectedOrder.equals("asc"))
            return 0
        else if (!selectedOrder.isNullOrEmpty() && selectedOrder.equals("dsc"))
            return 1
        else
            return -1
    }



    companion object {
        @JvmStatic
        fun newInstance(category: Category): SubCategoriesFragment {
            val fragment = SubCategoriesFragment()
            val bundle = Bundle()
            bundle.putSerializable("category", category)
            fragment.arguments = bundle
            return fragment
        }
    }


}
