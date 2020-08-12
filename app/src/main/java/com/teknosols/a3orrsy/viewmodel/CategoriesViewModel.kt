package com.teknosols.a3orrsy.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.datamodel.model.fan.User
import com.teknosols.a3orrsy.datamodel.model.response.ApiErrorResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.CategoriesResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.UserResponse
import com.teknosols.a3orrsy.datamodel.source.DataRepository
import com.teknosols.a3orrsy.datamodel.source.UserDataSource
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.isOnline
import kotlin.contracts.contract


class CategoriesViewModel(context: Application, private val dataRepository: DataRepository) :
    BaseAndroidViewModel(context) {

    var categories: MutableLiveData<OneShotEvent<CategoriesResponse>> = MutableLiveData()

    fun getCategories() {
        if (isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.getCategories(object : UserDataSource.GetCategoriesCallback {
                override fun onCategory(c: CategoriesResponse) {
                    showProgressBar(false)
                    categories.value = OneShotEvent(c)
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