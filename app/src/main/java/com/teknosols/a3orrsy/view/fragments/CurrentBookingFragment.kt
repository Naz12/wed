package com.teknosols.a3orrsy.view.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.smarteist.autoimageslider.DefaultSliderView
import com.smarteist.autoimageslider.IndicatorAnimations
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Service
import com.teknosols.a3orrsy.datamodel.model.fan.SubCategory
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.obtainViewModel
import com.teknosols.a3orrsy.other.extensions.showMapDirection
import com.teknosols.a3orrsy.other.extensions.showUpdateMessage
import com.teknosols.a3orrsy.other.util.DatePickerF
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.adapters.ServicesAdapter
import com.teknosols.a3orrsy.view.decoration.ListSpacingDecoration
import com.teknosols.a3orrsy.viewmodel.CurrentBookingViewModel
import kotlinx.android.synthetic.main.activity_global_navigation.*
import kotlinx.android.synthetic.main.confirmation_dialogue.*
import kotlinx.android.synthetic.main.fragment_current_booking.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Callable

class CurrentBookingFragment : BaseFragment(), View.OnClickListener,
    ServicesAdapter.ItemClickListener {
    override fun openDetail(subCategory: SubCategory, service: Service, status: String) {

        if (status == "service_detail") {
            showServiceDetailDialog(service)
        } else {
            showProgressDialog(true)
            this.subCategory = subCategory
            var sTAmount = 0.0
            for (x in 0 until subCategory.services.size) {
                if (subCategory.services[x].my_status) {
                    sTAmount =
                        sTAmount + (subCategory.services[x].my_quantity!!.toDouble() * subCategory.services[x].service_price!!.toDouble())
                }
            }
            servicesTAmount = sTAmount
            tvAmount.text = (amount + servicesTAmount).toString()
            showProgressDialog(false)
        }
    }

    var timeOptions: List<String> = listOf()
    var typeOptions: List<String> = listOf()
    var statusOptions = arrayOf("Temporary", "Confirmed")
    lateinit var subCategory: SubCategory
    lateinit var searchedBy: String
    var simpleDateFormat = SimpleDateFormat("dd-M-yyyy")

    var amount: Double = 0.0
    var servicesTAmount = 0.0

    lateinit var startDate: String
    lateinit var endDate: String
    lateinit var time: String
    lateinit var noDays: String
    lateinit var categoryId: String
    lateinit var adapter: ServicesAdapter
    var dialog_alert: AlertDialog? = null

    lateinit var viewModel: CurrentBookingViewModel

    override fun onClick(v: View?) {
        when (v) {
            rlMap -> {
                showMapDirection(context!!, subCategory.lat.toDouble(), subCategory.lng.toDouble())
            }

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
                //endregion
            }

            tvConfirmBooking -> {
                showConformationDialog()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            this.subCategory = it!!["subCategory"] as SubCategory
            this.searchedBy = it!!["searchedBy"] as String
            this.categoryId = it!!["categoryId"] as String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_current_booking, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiData()
        setSliderViews()
        setSpinnerData()
        setClickListeners()
        viewModelHandler()
    }


    fun setAdapter() {
        adapter = ServicesAdapter(context!!, subCategory, this)
        rvServices?.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            it.addItemDecoration(ListSpacingDecoration(resources.getDimensionPixelSize(R.dimen.recycler_view_spacing)))
            it.adapter = adapter
            if (it.itemDecorationCount > 1) {
                it.removeItemDecorationAt(0)
            }
        }
    }

    private fun setSliderViews() {
        for (i in 0 until subCategory.images.size) {
            val sliderView = DefaultSliderView(context)

            sliderView.imageUrl = subCategory.images.get(i).image
//            sliderView.setImageDrawable(R.drawable.wedding_img3)

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY)
//            sliderView.description = "setDescription " + (i + 1)
            sliderView.setOnSliderClickListener {
                val imageViewerFragment = ImageViewerFragment.newInstance(subCategory.images, i)
                (activity as BaseActivity).replaceFragment(
                    imageViewerFragment,
                    imageViewerFragment.getSimpleName(),
                    true,
                    false
                )
            }
            //at last add this view in your layout :
            imageSlider.addSliderView(sliderView)
            rlMap.setOnClickListener(this)
        }

        imageSlider.setIndicatorAnimation(IndicatorAnimations.THIN_WORM) //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setScrollTimeInSec(2) //set scroll delay in seconds :
    }

    private fun uiData() {
        (activity as GlobalNavigationActivity).toolbarLayout.header_textview.text = subCategory.name
        category_phone_no.text = subCategory.phone
        category_location.text = subCategory.address
        tvBookingPrice.text = subCategory.price
        tvSize.text = subCategory.size
        tvDetails.text = subCategory.details
        rating.rating = subCategory.rating


        if (subCategory.category_id == 3){//if category is Makeup Artist then remove per Day price
            llPerDayPrice.visibility = View.GONE
        }

        if (searchedBy == "Name")
            llSearch.visibility = View.VISIBLE
        else {
            llSearch.visibility = View.GONE
            tvAmount.text = (amount + servicesTAmount).toString()
        }

        category_location.isSelected = true
        setAdapter()
    }

    private fun setSpinnerData() {
        // Time Spinner
        if ((!subCategory.event_time.isNullOrEmpty())) {
            timeOptions = commaSaperate(subCategory.event_time.toString())
            val timeAdapter = ArrayAdapter<String>(context, R.layout.spinner_item, timeOptions)
            timeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spTime.setAdapter(timeAdapter)
        }
        // Type Spinner
        if ((!subCategory.event_type.isNullOrEmpty())) {
            typeOptions = commaSaperate(subCategory.event_type.toString())
            val typeAdapter = ArrayAdapter<String>(context, R.layout.spinner_item, typeOptions)
            typeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spType.setAdapter(typeAdapter)
        }
        // Status Spinner
        val statusAdapter = ArrayAdapter<String>(context, R.layout.spinner_item, statusOptions)
        statusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spStatus.setAdapter(statusAdapter)
        //Type of Payment Spinner
        val paymentTypeAdapter = ArrayAdapter<String>(context, R.layout.spinner_item, resources.getStringArray(R.array.type_of_payment))
        paymentTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spTypeOfPayment.setAdapter(paymentTypeAdapter)

    }

    private fun commaSaperate(targetString: String): List<String> {
        val listOf = targetString.split(',').map { it.trim() }
        return listOf
    }

    private fun setClickListeners() {
        rlMap.setOnClickListener(this)
        startdate.setOnClickListener(this)
        enddate.setOnClickListener(this)
        tvConfirmBooking.setOnClickListener(this)
    }

    fun datesDifferenceHandler() {
        try {

            if (!startdate.text.isNullOrEmpty() && !startdate.text.isNullOrEmpty()) {
                val date1 = simpleDateFormat.parse(startdate.text.toString())
                val date2 = simpleDateFormat.parse(startdate.text.toString())
                printDifference(date1, date2)
            }

        } catch (e: ParseException) {
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
            amount = days.toDouble() * subCategory.price.toDouble()
            tvAmount.text = (amount + servicesTAmount).toString()
        } else {
            tvNoOfDays.text = "0"
            showAlertDialog(getString(R.string.invalid_dates))
        }
    }

    fun viewModelHandler() {
        viewModel = obtainViewModel(CurrentBookingViewModel::class.java)

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

        viewModel.baseResponse.observe(
            viewLifecycleOwner,
            object : Observer<OneShotEvent<BaseResponse>> {
                override fun onChanged(t: OneShotEvent<BaseResponse>?) {
                    var show = t?.getContentIfNotHandled()
                    show?.let {
                        (activity as BaseActivity).showAlertDialog(it.message)
                        clearStack()
                    }
                }
            })

    }

    fun onAddBooking(): Int {
        var services_ids = ""
        var services_quantities = ""
        for (x in 0 until subCategory.services.size) {
            if (subCategory.services[x].my_status) {
                if (x == 0) {
                    services_ids = subCategory.services[x].id.toString()
                    services_quantities = subCategory.services[x].my_quantity!!
                } else {
                    services_ids = "${services_ids},${subCategory.services[x].id}"
                    services_quantities =
                        "${services_quantities},${subCategory.services[x].my_quantity!!}"
                }
            }
        }

        Log.i("myServicesIds", services_ids)
        Log.i("myServicesQuantities", services_quantities)

        if (searchedBy == "Name") {
            if (tvNoOfDays.text.toString().toInt() > 0) {
                viewModel.addBooking(
                    categoryId,
                    subCategory.id.toString(),
                    sessionManager.getUserId(),
                    sessionManager.getPhone(),
                    spType.selectedItem.toString(),
                    startdate.text.toString(),
                    startdate.text.toString(),
                    spTime.selectedItem.toString(),
                    tvAmount.text.toString(),
                    subCategory.city,
                    services_ids,
                    services_quantities
                )
            } else {
                showAlertDialog(getString(R.string.invalid_dates))
            }

        } else {
            if (noDays.toInt() > 0) {
                viewModel.addBooking(
                    categoryId,
                    subCategory.id.toString(),
                    sessionManager.getUserId(),
                    sessionManager.getPhone(),
                    spType.selectedItem.toString(),
                    startDate,
                    startDate,
                    time,
                    tvAmount.text.toString(),
                    subCategory.city,
                    services_ids,
                    services_quantities
                )
            } else {
                showAlertDialog(getString(R.string.invalid_dates))
            }

        }
        return 0
    }


    companion object {
        @JvmStatic
        fun newInstance(
            subCategory: SubCategory,
            searchedBy: String,
            categoryId: String
        ): CurrentBookingFragment {
            var fragment = CurrentBookingFragment()
            var bundle = Bundle()
            bundle.putSerializable("subCategory", subCategory)
            bundle.putString("searchedBy", searchedBy)
            bundle.putString("categoryId", categoryId)
            fragment.arguments = bundle
            return fragment
        }

        @JvmStatic
        fun newInstance(
            subCategory: SubCategory,
            searchedBy: String,
            startDate: String,
            endDate: String,
            time: String,
            noDays: String,
            categoryId: String
        ): CurrentBookingFragment {
            var fragment = CurrentBookingFragment()
            var bundle = Bundle()
            bundle.putSerializable("subCategory", subCategory)
            bundle.putString("searchedBy", searchedBy)
            bundle.putString("categoryId", categoryId)

            fragment.startDate = startDate
            fragment.endDate = endDate
            fragment.time = time
            fragment.noDays = noDays

            fragment.amount = noDays.toDouble() * subCategory.price.toDouble()

            fragment.arguments = bundle
            return fragment
        }
    }

    fun showServiceDetailDialog(service: Service): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(context!!)

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.service_detail, null)
        dialogBuilder.setView(dialogView)

        var name: TextView = dialogView.findViewById(R.id.tvName)
        var price: TextView = dialogView.findViewById(R.id.tvPrice)
        var detail: TextView = dialogView.findViewById(R.id.tvDetail)
        var kind: TextView = dialogView.findViewById(R.id.tvService)
        var cancelBtn: RelativeLayout = dialogView.findViewById(R.id.rlCancelBtn)

        name.text = service.service_name
        price.text = service.service_price
        detail.text = service.service_detail

        if (service.service_kind == "mandatory") {
            kind.text = getString(R.string.mandatory)
        } else {
            kind.text = getString(R.string.non_mandatory)
        }

        cancelBtn.setOnClickListener(View.OnClickListener {
            dialog_alert!!.dismiss()
        })

        dialog_alert = dialogBuilder.create()
        dialog_alert?.show()
        return dialog_alert!!
    }

    fun showConformationDialog(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(context!!)

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.confirmation_dialogue, null)
        dialogBuilder.setView(dialogView)

        var price: TextView = dialogView.findViewById(R.id.tvConfirmationPrice)
        var cash: RelativeLayout = dialogView.findViewById(R.id.rlTypeCash)
        var bank: RelativeLayout = dialogView.findViewById(R.id.rlTypeBank)
        var creditCard: RelativeLayout = dialogView.findViewById(R.id.rlTypeCreditCard)

        var terms : TextView = dialogView.findViewById(R.id.tvTermsAndCondition)
        var confirm : TextView = dialogView.findViewById(R.id.tvConfirm)
        var checkBox: CheckBox = dialogView.findViewById(R.id.cbTermsAndCondition)
        var cancelBtn: RelativeLayout = dialogView.findViewById(R.id.rlConfirmationCancelBtn)

        price.text = tvAmount.text.toString()
        confirm.setOnClickListener(View.OnClickListener {
            if(checkBox.isChecked){
                dialog_alert!!.dismiss()
                onAddBooking()
            }else{
                Toast.makeText(context,getString(R.string.check_terms),Toast.LENGTH_SHORT).show()
                checkBox.requestFocus()
            }

        })
        cancelBtn.setOnClickListener(View.OnClickListener {
            dialog_alert!!.dismiss()
        })

        bank.setOnClickListener {
            Toast.makeText(context!!, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }

        creditCard.setOnClickListener {
            Toast.makeText(context!!, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }

        dialog_alert = dialogBuilder.create()
        dialog_alert?.show()
        return dialog_alert!!
    }

}
