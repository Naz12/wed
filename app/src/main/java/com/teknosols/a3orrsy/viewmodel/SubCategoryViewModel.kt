package com.teknosols.a3orrsy.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.User
import com.teknosols.a3orrsy.datamodel.model.response.ApiErrorResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.CitiesResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.SubCategoriesResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.UserResponse
import com.teknosols.a3orrsy.datamodel.source.DataRepository
import com.teknosols.a3orrsy.datamodel.source.UserDataSource
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.isOnline
import com.teknosols.a3orrsy.other.extensions.parseDate


class SubCategoryViewModel(context: Application, private val dataRepository: DataRepository) :
    BaseAndroidViewModel(context) {

    var subCategory: MutableLiveData<OneShotEvent<SubCategoriesResponse>> = MutableLiveData()
    var availableSubCategory: MutableLiveData<OneShotEvent<SubCategoriesResponse>> = MutableLiveData()
    var citiesResponse: MutableLiveData<OneShotEvent<CitiesResponse>> = MutableLiveData()

    fun getSubCategories(category_id: String) {
        if(isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.getSubCategories(category_id, object : UserDataSource.GetSubCategoriesCallback {
                override fun onSubCategory(s: SubCategoriesResponse) {
                    showProgressBar(false)
                    subCategory.value = OneShotEvent(s)
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

    fun getAvailableSubCategories(category_id: String, start_date: String, end_date: String, time: String, city_name: String) {
        if(isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.getAvailableSubCategories(category_id,  parseDate(start_date)!!, parseDate(end_date)!!, time, city_name, object : UserDataSource.GetAvailableSubCategoriesCallback {
                override fun onSubCategory(s: SubCategoriesResponse) {
                    showProgressBar(false)
                    availableSubCategory.value = OneShotEvent(s)
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

    fun getCities() {
        if(isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.getCities(object : UserDataSource.GetCitiesCallback {
                override fun onGetting(c: CitiesResponse) {
                    showProgressBar(false)
                    citiesResponse.value = OneShotEvent(c)
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