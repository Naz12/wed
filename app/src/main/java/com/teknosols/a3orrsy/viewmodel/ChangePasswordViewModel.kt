package com.teknosols.a3orrsy.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.response.ApiErrorResponse
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import com.teknosols.a3orrsy.datamodel.source.DataRepository
import com.teknosols.a3orrsy.datamodel.source.UserDataSource
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.isOnline


class ChangePasswordViewModel(context: Application, private val dataRepository: DataRepository) :
    BaseAndroidViewModel(context) {

    var baseResponse: MutableLiveData<OneShotEvent<BaseResponse>> = MutableLiveData()

    fun changePassword(user_id: String, old_password: String, new_password: String) {
        if(isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.changePassword(user_id, old_password,new_password, object : UserDataSource.ChangePasswordCallback {
                override fun onChange(b: BaseResponse) {
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

    fun userForgotPassword(phoneNo : String , new_password: String){
        if(isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.userForgotPassword(phoneNo , new_password ,  object : UserDataSource.UserForgotPassword{
                override fun onChangePassword(response: BaseResponse) {
                    showProgressBar(false)
                    baseResponse.value = OneShotEvent(response)
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