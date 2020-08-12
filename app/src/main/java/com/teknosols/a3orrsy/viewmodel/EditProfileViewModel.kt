package com.teknosols.a3orrsy.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.response.ApiErrorResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.ImageResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.UserResponse
import com.teknosols.a3orrsy.datamodel.source.DataRepository
import com.teknosols.a3orrsy.datamodel.source.UserDataSource
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.isOnline


class EditProfileViewModel(context: Application, private val dataRepository: DataRepository) :
    BaseAndroidViewModel(context) {

    var imageResponse: MutableLiveData<OneShotEvent<ImageResponse>> = MutableLiveData()
    var updatedUser: MutableLiveData<OneShotEvent<UserResponse>> = MutableLiveData()

    fun uploadImage(user_id: String, avatar: String) {
        if(isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.uploadImage(user_id, avatar, object : UserDataSource.UploadImageCallback {
                override fun onUpload(u: ImageResponse) {
                    showProgressBar(false)
                    imageResponse.value = OneShotEvent(u)
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

    fun updateUserProfile(user_id: String, name: String, phone: String, email: String, birthday: String, address: String, city: String, country: String) {
        if(isOnline(getContext())) {
            showProgressBar(true)
            dataRepository.updateUserProfile(user_id, name, phone, email, birthday, address, city, country, object : UserDataSource.UpdateUserProfileCallback {
                override fun onUpdate(u: UserResponse) {
                    showProgressBar(false)
                    updatedUser.value = OneShotEvent(u)
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