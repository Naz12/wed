package com.teknosols.a3orrsy.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.User
import com.teknosols.a3orrsy.datamodel.model.response.ApiErrorResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.UserResponse
import com.teknosols.a3orrsy.datamodel.source.DataRepository
import com.teknosols.a3orrsy.datamodel.source.UserDataSource
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.isOnline


class SignInViewModel(context: Application, private val dataRepository: DataRepository) :
    BaseAndroidViewModel(context) {

    var user: MutableLiveData<OneShotEvent<UserResponse>> = MutableLiveData()

    fun  login(phoneNo: String, password: String, token: String) {
        if(isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.userLogin(phoneNo, password,token, object : UserDataSource.LoginCallback {
                override fun onLogin(u: UserResponse) {
                    showProgressBar(false)
                    user.value = OneShotEvent(u)
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