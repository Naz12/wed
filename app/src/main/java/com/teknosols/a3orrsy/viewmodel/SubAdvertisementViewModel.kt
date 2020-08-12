package com.teknosols.a3orrsy.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.teknosols.a3orrsy.datamodel.model.response.ApiErrorResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.SubCategoriesResponse
import com.teknosols.a3orrsy.datamodel.source.DataRepository
import com.teknosols.a3orrsy.datamodel.source.UserDataSource
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.isOnline

class SubAdvertisementViewModel(context: Application, private val dataRepository: DataRepository):
BaseAndroidViewModel(context){

    var advertisement : MutableLiveData<OneShotEvent<SubCategoriesResponse>> = MutableLiveData()

    fun getAllAdvertisement(category_id : String){

        if(isOnline(getContext())){
            showProgressBar(true)
            dataRepository.getAllAdvertisement(category_id , object : UserDataSource.GetAllAdvertisement{
                override fun onAdvertisement(advertisementResponse: SubCategoriesResponse) {
                    showProgressBar(false)
                    advertisement.value = OneShotEvent(advertisementResponse)
                }

                override fun onPayloadError(error: ApiErrorResponse) {
                    showProgressBar(false)
                    showSnackbarMessage(error.message)
                }

            })
        }
    }
}