package com.teknosols.a3orrsy.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.net.ConnectivityManager
import com.teknosols.a3orrsy.datamodel.model.error.ErrorResponse
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.view.dialog.AlertMessageDialog
import kotlin.coroutines.coroutineContext
import kotlin.math.absoluteValue


const val ERROR_CODE_FALSE = 0
const val ERROR_CODE_WRONG_PASSWORD = -10
const val ERROR_CODE_INVALID_EMAIL = -11
const val ERROR_CODE_INVALID_PHONE = -12
const val ERROR_CODE_NO_INTERNET = -13

open class BaseAndroidViewModel(context: Application) : AndroidViewModel(context) {

    init {

    }

    val snackbarMessage = MutableLiveData<OneShotEvent<String>>()
    val errorResponse = MutableLiveData<OneShotEvent<ErrorResponse>>()
    val progressBar = MutableLiveData<OneShotEvent<Boolean>>()


    protected fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }

    protected fun getString(resId: Int): String{
        return getContext().getString(resId)
    }

    protected fun showSnackbarMessage(message: String) {
        snackbarMessage.value = OneShotEvent(message)
    }

    protected fun handleErrorType(errorType: Int, errorMessage: String){
        val error  = ErrorResponse(errorMessage, errorType)
        this.errorResponse.value = OneShotEvent(error)
    }

    protected fun showProgressBar(show: Boolean){
        progressBar.value = OneShotEvent(show)
    }
}