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
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.smarteist.autoimageslider.DefaultSliderView
import com.smarteist.autoimageslider.IndicatorAnimations
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Booking
import com.teknosols.a3orrsy.datamodel.model.fan.BookingServices
import com.teknosols.a3orrsy.datamodel.model.fan.Service
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.BookingResponse
import com.teknosols.a3orrsy.other.eventbus.EventBusFactory
import com.teknosols.a3orrsy.other.extensions.*
import com.teknosols.a3orrsy.view.activites.AdminActivity
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.adapters.AdminServicesAdapter
import com.teknosols.a3orrsy.view.adapters.InvoiceAdapter
import com.teknosols.a3orrsy.view.adapters.ServicesAdapter
import com.teknosols.a3orrsy.view.decoration.ListSpacingDecoration
import com.teknosols.a3orrsy.viewmodel.AdminBookingDetailViewModel
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.fragment_admin_booking_detail.*
import kotlinx.android.synthetic.main.fragment_admin_booking_detail.category_location
import kotlinx.android.synthetic.main.fragment_admin_booking_detail.category_phone_no
import kotlinx.android.synthetic.main.fragment_admin_booking_detail.imageSlider
import kotlinx.android.synthetic.main.fragment_admin_booking_detail.rlMap
import kotlinx.android.synthetic.main.fragment_admin_booking_detail.tvType
import kotlinx.android.synthetic.main.fragment_current_booking.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import java.util.concurrent.Callable


class AdminBookingDetailFragment : BaseFragment(), View.OnClickListener, AdminDepositFragment.OnUpdateDeposit , AdminServicesAdapter.ItemClickListener {
    override fun openDetail(booking: Booking, service: BookingServices, status: String) {

        if(status == "service_detail"){
            showServiceDetailDialog(service)
        }
        else {
            showProgressDialog(true)
            this.booking = booking
            var sTAmount = 0.0
            servicesTAmount = sTAmount
            tvAmount.text = (amount + servicesTAmount).toString()
            showProgressDialog(false)
        }
    }

    override fun onUpdate(amount: String) {
        viewModel.updateBookingDeposit(booking.id.toString(), amount)
    }

    lateinit var booking: Booking
    lateinit var viewModel: AdminBookingDetailViewModel
    var deposit: Double = 0.0
    lateinit var adapter: InvoiceAdapter
    lateinit var loginAs: String
    lateinit var servicesAdapter : AdminServicesAdapter
    var dialog_alert: AlertDialog? = null
    var amount: Double = 0.0
    var servicesTAmount = 0.0

    override fun onClick(v: View?) {
        when (v) {
            rlMap -> {
                showMapDirection(
                    context!!,
                    booking.event_details!!.lat.toDouble(),
                    booking.event_details!!.lng.toDouble()
                )
            }
            tvDeposit -> {

                val adminDepositFragment = AdminDepositFragment.newInstance(this)
                (activity as BaseActivity).replaceFragment(
                    adminDepositFragment,
                    adminDepositFragment.getSimpleName(),
                    true,
                    false
                )
            }
            btnComplete -> {
                when (booking.booking_status) {
                    "inprogress" -> {           // 1
                        showUpdateMessage(context!!, getString(R.string.inprogress_msg), Callable<Int> { updateBookingComplete("completed") })
                    }
                    "advance_paid" -> {         // 2
                        showUpdateMessage(context!!, getString(R.string.advance_paid_msg), Callable<Int> { updateBookingComplete("completed") })
                    }
                    "fully_paid" -> {           // 3
                        showUpdateMessage(context!!, getString(R.string.fully_paid_msg), Callable<Int> { updateBookingComplete("completed") })
                    }
                }
            }
            btnCancel ->{
                showUpdateMessage(context!!, getString(R.string.cancelled_msg), Callable<Int> { updateBookingComplete("cancelled_non_payment") })
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            this.booking = it!!["booking"] as Booking
        }

        loginAs = (activity as BaseActivity).sessionManager.getLoginAs()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_admin_booking_detail, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelHandler()
        setClickListeners()
        setUI()

    }

    private fun setSliderViews() {
        imageSlider.clearSliderViews()
        for (i in 0 until booking.event_details!!.images.size) {
            val sliderView = DefaultSliderView(context)

            sliderView.imageUrl = booking.event_details!!.images.get(i).image
//            sliderView.setImageDrawable(R.drawable.wedding_img3)

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY)
//            sliderView.description = "setDescription " + (i + 1)
            sliderView.setOnSliderClickListener {

            }
            //at last add this view in your layout :
            imageSlider.addSliderView(sliderView)
        }

        imageSlider.setIndicatorAnimation(IndicatorAnimations.THIN_WORM) //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setScrollTimeInSec(2) //set scroll delay in seconds
    }

    private fun setUI() {
        if (loginAs.equals("customer"))
            (activity as GlobalNavigationActivity).toolbarLayout.header_textview.text = booking.event_details!!.name
        else
            (activity as AdminActivity).toolbarLayout.header_textview.text = booking.event_details!!.name

        setSliderViews()

        // Event Detail
        category_phone_no.text = booking.event_details!!.phone
        category_location.text = booking.event_details!!.address

        // Customer Detail
        tvName.text = booking.user_details!!.name
        tvPhone.text = booking.user_details!!.phone
        tvEmail.text = booking.user_details!!.email
        tvDateOfBirth.text = booking.user_details!!.birthday
        tvAddress.text = booking.user_details!!.address
        tvCity.text = booking.user_details!!.city
        tvCountry.text = booking.user_details!!.country

        // Booking Detail
        tvBookingDate.text = booking.current_date_time
        tvSatrtDate.text = parseDateIntoAppFormat(booking.date_from!!)
        tvEndDate.text = parseDateIntoAppFormat(booking.date_to!!)
        tvType.text = booking.type!!
        tvTotalPrice.text = booking.price!!

        deposit = 0.0
        for (x in 0 until booking.deposit_history!!.size) {
            deposit = deposit + booking.deposit_history!!.get(x).deposit_amount!!.toDouble()
        }
        tvDeposit.text = deposit.toString()

        if (deposit >= 0 && deposit < booking.price!!.toDouble()) {
            var remaining = booking.price!!.toDouble() - deposit
            tvRemainingPrice.text = String.format("%.2f", remaining)
        } else {
            tvRemainingPrice.text = "0"
        }

        if (loginAs.equals("customer")) {

            tvDeposit.setOnClickListener(null)
            tvDeposit.setCompoundDrawables(null, null, null, null)
            btnComplete.visibility = View.GONE
            btnCancel.visibility = View.GONE
        }

        setAdapter()
        bookingStatusHandler()

    }


    private fun setClickListeners() {
        rlMap.setOnClickListener(this)
        btnComplete.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        tvDeposit.setOnClickListener(this)
    }

    fun bookingStatusHandler() {
        when (booking.booking_status) {
            "disabled " -> {            // 0

            }
            "inprogress" -> {           // 1
                tvBookingStatus.text = "In Processing"
                if (deposit > 0 && deposit < booking.price!!.toDouble()) {
                    viewModel.updateBookingStatus(booking.id.toString(), "advance_paid")
                } else if (deposit >= booking.price!!.toDouble()) {
                    viewModel.updateBookingStatus(booking.id.toString(), "fully_paid")
                }
            }
            "advance_paid" -> {         // 2
                tvBookingStatus.text = "In Processing (Advance Paid)"
                if (deposit >= booking.price!!.toDouble()) {
                    viewModel.updateBookingStatus(booking.id.toString(), "fully_paid")
                }
            }
            "fully_paid" -> {           // 3
                tvBookingStatus.text = "Fully Paid"
                if (deposit > 0 && deposit < booking.price!!.toDouble()) {
                    viewModel.updateBookingStatus(booking.id.toString(), "advance_paid")
                } else if (deposit <= 0.0) {
                    viewModel.updateBookingStatus(booking.id.toString(), "inprogress")
                }
            }
            "completed" -> {             // 4
                tvBookingStatus.text = "Completed"
                btnComplete.visibility = View.GONE
                btnCancel.visibility = View.GONE
                tvDeposit.setOnClickListener(null)
                tvDeposit.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            "cancelled_by_client" -> {   // 5
                tvBookingStatus.text = "Cancelled By Customer"
                btnComplete.visibility = View.GONE
                btnCancel.visibility = View.GONE
                tvDeposit.setOnClickListener(null)
                tvDeposit.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            "cancelled_non_payment" -> {   // 6
                tvBookingStatus.text = "Cancelled Non Payment"
                btnComplete.visibility = View.GONE
                btnCancel.visibility = View.GONE
                tvDeposit.setOnClickListener(null)
                tvDeposit.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            else -> {
                Log.e(AdminBookingDetailFragment().getSimpleName(), "Invalid Status Found In Data")
            }
        }
    }

    fun viewModelHandler() {
        viewModel = obtainViewModel(AdminBookingDetailViewModel::class.java)

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

        viewModel.booking.observe(viewLifecycleOwner, object : Observer<OneShotEvent<BookingResponse>> {
            override fun onChanged(t: OneShotEvent<BookingResponse>?) {
                var show = t?.getContentIfNotHandled()
                show?.let {
                    booking = it.data
                    setUI()
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusFactory.getEventBus().post("OnDestroy")
    }


//    fun dansMethod(myFunc: Callable<String>) {
//        var str = myFunc.call()
//        Log.i("MyTesting", "dansMethod ${str}")
//    }

    fun updateBookingComplete(status: String): Int {
        viewModel.updateBookingStatus(booking.id.toString(), status)
        return 0
    }

    fun setAdapter() {
        adapter = InvoiceAdapter(context!!, booking!!.deposit_history!!)
        servicesAdapter = AdminServicesAdapter(context!! , booking , this)

        rvInvoice?.let {
            it.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            it.addItemDecoration(ListSpacingDecoration(resources.getDimensionPixelSize(R.dimen.recycler_view_spacing)))
            it.adapter = adapter
            if (it.itemDecorationCount > 1) {
                it.removeItemDecorationAt(0)
            }

        }

        rvAdminServices.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            it.addItemDecoration(ListSpacingDecoration(resources.getDimensionPixelSize(R.dimen.recycler_view_spacing)))
            it.adapter = servicesAdapter
            if (it.itemDecorationCount > 1) {
                it.removeItemDecorationAt(0)
            }
        }

    }

    fun showServiceDetailDialog(service: BookingServices): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(context!!)

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.service_detail, null)
        dialogBuilder.setView(dialogView)

        var name: TextView = dialogView.findViewById(R.id.tvName)
        var price: TextView = dialogView.findViewById(R.id.tvPrice)
        var detail: TextView = dialogView.findViewById(R.id.tvDetail)
        var kind: TextView = dialogView.findViewById(R.id.tvService)
        var cancelBtn: RelativeLayout = dialogView.findViewById(R.id.rlCancelBtn)

        name.text = service.bs_name
        price.text = service.bs_price
        detail.text = service.bs_detail


        cancelBtn.setOnClickListener(View.OnClickListener {
            dialog_alert!!.dismiss()
        })

        dialog_alert = dialogBuilder.create()
        dialog_alert?.show()
        return dialog_alert!!
    }


        companion object {
        @JvmStatic
        fun newInstance(booking: Booking): AdminBookingDetailFragment {
            var fragment = AdminBookingDetailFragment()
            var bundle = Bundle()
            bundle.putSerializable("booking", booking)
            fragment.arguments = bundle
            return fragment
        }


    }


}
