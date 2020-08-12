package com.befikey.user.other.factory

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting
import com.teknosols.a3orrsy.datamodel.source.DataRepository
import com.teknosols.a3orrsy.viewmodel.*


/**
 * A creator is used to inject the product ID into the ViewModel
 *
 *
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
class ViewModelFactory private constructor(
    private val application: Application,
    private val dataRepository: DataRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(SignupViewModel::class.java) ->
                    SignupViewModel(application, dataRepository)
                isAssignableFrom(SignInViewModel::class.java) ->
                    SignInViewModel(application, dataRepository)
                isAssignableFrom(EditProfileViewModel::class.java) ->
                    EditProfileViewModel(application, dataRepository)
                isAssignableFrom(CategoriesViewModel::class.java) ->
                    CategoriesViewModel(application, dataRepository)
                isAssignableFrom(ChangePasswordViewModel::class.java) ->
                    ChangePasswordViewModel(application, dataRepository)
                isAssignableFrom(SubCategoryViewModel::class.java) ->
                    SubCategoryViewModel(application, dataRepository)
                isAssignableFrom(CurrentBookingViewModel::class.java) ->
                    CurrentBookingViewModel(application, dataRepository)
                isAssignableFrom(AdminHomeViewModel::class.java) ->
                    AdminHomeViewModel(application, dataRepository)
                isAssignableFrom(AdminBookingDetailViewModel::class.java) ->
                    AdminBookingDetailViewModel(application, dataRepository)
                isAssignableFrom(FavEventsViewModel::class.java) ->
                    FavEventsViewModel(application , dataRepository)
                isAssignableFrom(SubAdvertisementViewModel::class.java) ->
                    SubAdvertisementViewModel(application,dataRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE
                ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE
                    ?: ViewModelFactory(
                        application,
                        DataRepository(application.applicationContext)
                    )
                    .also { INSTANCE = it }
            }


        @VisibleForTesting fun destroyInstance() {
            INSTANCE = null
        }
    }
}
