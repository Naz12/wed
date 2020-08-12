package com.teknosols.a3orrsy.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.response.ApiErrorResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.AllBookingsResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.SubCategoriesResponse
import com.teknosols.a3orrsy.datamodel.source.DataRepository
import com.teknosols.a3orrsy.datamodel.source.UserDataSource
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.isOnline


class FavEventsViewModel(context: Application, private val dataRepository: DataRepository) :
    BaseAndroidViewModel(context) {

    var allBookingsResponse: MutableLiveData<OneShotEvent<SubCategoriesResponse>> = MutableLiveData()

    fun getAllFavEvents() {
        if (isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.getAllFavEvents(object : UserDataSource.GetAllFavEventsCallback {
                override fun onFavEvents(b: SubCategoriesResponse) {
                    showProgressBar(false)
                    allBookingsResponse.value = OneShotEvent(b)
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