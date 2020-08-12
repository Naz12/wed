package com.teknosols.a3orrsy.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.User
import com.teknosols.a3orrsy.datamodel.model.response.ApiErrorResponse
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.SubCategoriesResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.UserResponse
import com.teknosols.a3orrsy.datamodel.source.DataRepository
import com.teknosols.a3orrsy.datamodel.source.UserDataSource
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.isOnline
import com.teknosols.a3orrsy.other.extensions.parseDate


class CurrentBookingViewModel(context: Application, private val dataRepository: DataRepository) :
    BaseAndroidViewModel(context) {

    var baseResponse: MutableLiveData<OneShotEvent<BaseResponse>> = MutableLiveData()

    fun addBooking(category_id: String, event_id: String, user_id: String, user_phone: String, type: String, start_date: String, end_date: String, time: String, total_price: String, city: String, services_ids: String, services_quantities: String) {
        if(isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.addBooking(category_id, event_id, user_id, user_phone, type, parseDate(start_date)!!, parseDate(end_date)!!, time, total_price, city, services_ids, services_quantities, object : UserDataSource.AddBookingCallback {
                override fun onBooking(b: BaseResponse) {
                    showProgressBar(false)
                    baseResponse.value = OneShotEvent(b)
                }
                override fun onPayloadError(error: ApiErrorResponse) {
                    showProgressBar(false)
                    showSnackbarMessage(error.message)
                }
            })
        }else {
            showSnackbarMessage(getString(R.string.internet_msg))
        }

    }
}