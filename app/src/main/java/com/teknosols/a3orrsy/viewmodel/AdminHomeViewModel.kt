package com.teknosols.a3orrsy.viewmodel

import android.app.Application
import android.app.Notification
import android.arch.lifecycle.MutableLiveData
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.fan.Notifications
import com.teknosols.a3orrsy.datamodel.model.response.ApiErrorResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.AllBookingsResponse
import com.teknosols.a3orrsy.datamodel.source.DataRepository
import com.teknosols.a3orrsy.datamodel.source.UserDataSource
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.isOnline


class AdminHomeViewModel(context: Application, private val dataRepository: DataRepository) :
    BaseAndroidViewModel(context) {

    var allBookingsResponse: MutableLiveData<OneShotEvent<AllBookingsResponse>> = MutableLiveData()
    var notificationResponse: MutableLiveData<OneShotEvent<Notifications>> = MutableLiveData()

    fun getAllBookings() {
        if (isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.getAllBookings(object : UserDataSource.GetAllBookingCallback {
                override fun onGetBooking(b: AllBookingsResponse) {
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

    fun  getAllUserBookings(user_id : String){

        if (isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.getAllUserBookings(user_id , object : UserDataSource.GetAllUserBookingsCallback{
                override fun onGetUserBooking(b: AllBookingsResponse) {
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

    fun getAllUserNotifications(user_id: String){

        if(isOnline(getContext())){

            dataRepository.getAllUserNotifications(user_id, object :UserDataSource.GetAllUserNotifications{
                override fun onNotification(allUserNotificationsResponse: AllBookingsResponse) {
                    showProgressBar(false)
                    allBookingsResponse.value = OneShotEvent(allUserNotificationsResponse)
                }

                override fun onPayloadError(error: ApiErrorResponse) {
                    showProgressBar(false)
                    showSnackbarMessage(error.message)
                }

            })
        }else{
            showSnackbarMessage(getString(R.string.internet_msg))
        }
    }

    fun updateNotificationStatus(user_id: String , notify_id: String , status:String){

        if(isOnline(getContext())){
            dataRepository.updateNotificationStatus(user_id,notify_id,status, object :UserDataSource.UpdateNotificationStatus{
                override fun onUpdate(updateResponse: Notifications) {
                    showProgressBar(false)
                    notificationResponse.value = OneShotEvent(updateResponse)
                }

                override fun onPayloadError(error: ApiErrorResponse) {
                    showProgressBar(false)
                    showSnackbarMessage(error.message)
                }

            })
        }else{
            showSnackbarMessage(getString(R.string.internet_msg))
        }

    }
}