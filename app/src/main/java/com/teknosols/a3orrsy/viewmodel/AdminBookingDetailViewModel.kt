package com.teknosols.a3orrsy.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.response.ApiErrorResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.AllBookingsResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.BookingResponse
import com.teknosols.a3orrsy.datamodel.source.DataRepository
import com.teknosols.a3orrsy.datamodel.source.UserDataSource
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.isOnline


class AdminBookingDetailViewModel(context: Application, private val dataRepository: DataRepository) :
    BaseAndroidViewModel(context) {

    var booking: MutableLiveData<OneShotEvent<BookingResponse>> = MutableLiveData()

    fun updateBookingStatus(booking_id: String, status: String) {
        if (isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.updateBookingStatus(booking_id,status, object : UserDataSource.UpdateBookingStatusCallback {
                override fun onUpdate(b: BookingResponse) {
                    showProgressBar(false)
                    booking.value = OneShotEvent(b)
                }

                override fun onPayloadError(error: ApiErrorResponse) {
                    showProgressBar(false)
                    showSnackbarMessage(error.message)
                }
            })

        } else {
            showSnackbarMessage(getString(R.string.internet_msg))
        }
    }

    fun updateBookingDeposit(booking_id: String, deposit: String) {
        if (isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.updateBookingDeposit(booking_id,deposit, object : UserDataSource.UpdateBookingStatusCallback {
                override fun onUpdate(b: BookingResponse) {
                    showProgressBar(false)
                    booking.value = OneShotEvent(b)
                }

                override fun onPayloadError(error: ApiErrorResponse) {
                    showProgressBar(false)
                    showSnackbarMessage(error.message)
                }
            })

        } else {
            showSnackbarMessage(getString(R.string.internet_msg))
        }
    }


}