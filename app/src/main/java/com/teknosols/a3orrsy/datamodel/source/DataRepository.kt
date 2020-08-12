package com.teknosols.a3orrsy.datamodel.source

import android.content.Context
import com.teknosols.a3orrsy.datamodel.model.fan.Notifications
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.*
import com.teknosols.a3orrsy.datamodel.source.Remote.ApiService
import com.teknosols.a3orrsy.datamodel.source.Remote.RetrofitClientInstance
import com.teknosols.a3orrsy.other.util.ErrorUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DataRepository(ctx: Context) {
    var context: Context

    init {
        context = ctx
    }


    fun getApiService(): ApiService {
        return RetrofitClientInstance.getInstance(context)!!.getService()
    }

    fun userRegister(
        name: String,
        phone: String,
        email: String,
        password: String,
        birthday: String,
        address: String,
        city: String,
        country: String,
        avatar: String,
        token: String,
        callback: UserDataSource.RegisterCallback
    ) {
        val name = RequestBody.create(MultipartBody.FORM, name)
        val phone = RequestBody.create(MultipartBody.FORM, phone)
        val email = RequestBody.create(MultipartBody.FORM, email)
        val password = RequestBody.create(MultipartBody.FORM, password)
        val birthday = RequestBody.create(MultipartBody.FORM, birthday)
        val address = RequestBody.create(MultipartBody.FORM, address)
        val city = RequestBody.create(MultipartBody.FORM, city)
        val country = RequestBody.create(MultipartBody.FORM, country)
        val token = RequestBody.create(MultipartBody.FORM, token)
        val MEDIA_TYPE_PNG = MediaType.parse("image/jpeg")
        val file = File(avatar)
        val requestBody = RequestBody.create(MEDIA_TYPE_PNG, file)
        val body = MultipartBody.Part.createFormData("avatar", file.name, requestBody)

        getApiService().register(name, phone, email, password, birthday, address, city, country, token, body)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        callback.onRegister(response.body()!!)
                    } else {
                        callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    callback.onPayloadError(ErrorUtils.parseError(t))
                }
            })
    }

    fun registerWithoutImage(
        name: String,
        phone: String,
        email: String,
        password: String,
        birthday: String,
        address: String,
        city: String,
        country: String,
        token: String,
        callback: UserDataSource.RegisterCallback
    ) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("name", name)
            it.put("phone", phone)
            it.put("email", email)
            it.put("password", password)
            it.put("birthday", birthday)
            it.put("address", address)
            it.put("city", city)
            it.put("country", country)
            it.put("token", token)
        }

        getApiService().registerWithoutImage(params).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    callback.onRegister(response.body()!!)
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun userLogin(phoneNo: String, password: String, token: String, callback: UserDataSource.LoginCallback) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("phone_number", phoneNo)
            it.put("password", password)
            it.put("token", token)
        }

        getApiService().userLogin(params).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onLogin(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun uploadImage(
        user_id: String,
        avatar: String,
        callback: UserDataSource.UploadImageCallback
    ) {
        val user_id = RequestBody.create(MultipartBody.FORM, user_id)
        val MEDIA_TYPE_PNG = MediaType.parse("image/jpeg")
        val file = File(avatar)
        val requestBody = RequestBody.create(MEDIA_TYPE_PNG, file)
        val body = MultipartBody.Part.createFormData("avatar", file.name, requestBody)

        getApiService().uploadImage(user_id, body).enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if (response.isSuccessful) {
                    callback.onUpload(response.body()!!)
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun updateUserProfile(
        user_id: String,
        name: String,
        phone: String,
        email: String,
        birthday: String,
        address: String,
        city: String,
        country: String,
        callback: UserDataSource.UpdateUserProfileCallback
    ) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("user_id", user_id)
            it.put("name", name)
            it.put("phone", phone)
            it.put("email", email)
            it.put("birthday", birthday)
            it.put("address", address)
            it.put("city", city)
            it.put("country", country)
        }

        getApiService().updateUserProfile(params).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onUpdate(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }


    fun getCategories(callback: UserDataSource.GetCategoriesCallback) {
        getApiService().getCategories().enqueue(object : Callback<CategoriesResponse> {
            override fun onResponse(call: Call<CategoriesResponse>, response: Response<CategoriesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onCategory(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun changePassword(
        user_id: String,
        old_password: String,
        new_password: String,
        callback: UserDataSource.ChangePasswordCallback
    ) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("user_id", user_id)
            it.put("old_password", old_password)
            it.put("new_password", new_password)
        }

        getApiService().changePassword(params).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onChange(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun getSubCategories(category_id: String, callback: UserDataSource.GetSubCategoriesCallback) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("category_id", category_id)
        }

        getApiService().getSubCategories(params).enqueue(object : Callback<SubCategoriesResponse> {
            override fun onResponse(call: Call<SubCategoriesResponse>, response: Response<SubCategoriesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSubCategory(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<SubCategoriesResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun getAvailableSubCategories(
        category_id: String,
        start_date: String,
        end_date: String,
        time: String,
        city_name: String,
        callback: UserDataSource.GetAvailableSubCategoriesCallback
    ) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("category_id", category_id)
            it.put("start_date", start_date)
            it.put("end_date", end_date)
            it.put("time", time)
            it.put("city_name", city_name)
        }

        getApiService().getAvailableSubCategories(params).enqueue(object : Callback<SubCategoriesResponse> {
            override fun onResponse(call: Call<SubCategoriesResponse>, response: Response<SubCategoriesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSubCategory(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<SubCategoriesResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun addBooking(
        category_id: String,
        event_id: String,
        user_id: String,
        user_phone: String,
        type: String,
        start_date: String,
        end_date: String,
        time: String,
        total_price: String,
        city: String,
        services_ids: String,
        services_quantities: String,
        callback: UserDataSource.AddBookingCallback
    ) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("category_id", category_id)
            it.put("event_id", event_id)
            it.put("user_id", user_id)
            it.put("user_phone", user_phone)
            it.put("type", type)
            it.put("start_date", start_date)
            it.put("end_date", end_date)
            it.put("time", time)
            it.put("total_price", total_price)
            it.put("city", city)
            it.put("services_ids", services_ids)
            it.put("services_quantities", services_quantities)
        }

        getApiService().addBooking(params).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onBooking(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun getAllBookings(callback: UserDataSource.GetAllBookingCallback) {
        getApiService().getAllBookings().enqueue(object : Callback<AllBookingsResponse> {
            override fun onResponse(call: Call<AllBookingsResponse>, response: Response<AllBookingsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onGetBooking(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<AllBookingsResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun getAllUserBookings(user_id: String , callback: UserDataSource.GetAllUserBookingsCallback){
        val params : HashMap<String , String> = HashMap()
        params.put("user_id" , user_id)

        getApiService().getAllUserBookings(params).enqueue(object : Callback<AllBookingsResponse>{
            override fun onResponse(call: Call<AllBookingsResponse>, response: Response<AllBookingsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onGetUserBooking(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<AllBookingsResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }

        })
    }

    fun updateBookingStatus(booking_id: String, status: String, callback: UserDataSource.UpdateBookingStatusCallback) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("booking_id", booking_id)
            it.put("status", status)
        }

        getApiService().updateBookingStatus(params).enqueue(object : Callback<BookingResponse> {
            override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onUpdate(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun updateBookingDeposit(
        booking_id: String,
        deposit: String,
        callback: UserDataSource.UpdateBookingStatusCallback
    ) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("booking_id", booking_id)
            it.put("deposit", deposit)
        }

        getApiService().updateBookingDeposit(params).enqueue(object : Callback<BookingResponse> {
            override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onUpdate(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun getCities(callback: UserDataSource.GetCitiesCallback) {

        getApiService().getCities().enqueue(object : Callback<CitiesResponse> {
            override fun onResponse(call: Call<CitiesResponse>, response: Response<CitiesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onGetting(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<CitiesResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun getAllFavEvents(callback: UserDataSource.GetAllFavEventsCallback) {

        getApiService().getAllFavEvents().enqueue(object: Callback<SubCategoriesResponse>{
            override fun onResponse(call: Call<SubCategoriesResponse>, response: Response<SubCategoriesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onFavEvents(it)
                    }
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<SubCategoriesResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }

        })
    }

    fun getAllUserNotifications(user_id: String, callback:  UserDataSource.GetAllUserNotifications){

        val params : HashMap<String,String> = HashMap()
        params.let {
            it.put("user_id",user_id)
        }
        getApiService().getAllUserNotifications(params).enqueue(object :Callback<AllBookingsResponse>{

            override fun onResponse(call: Call<AllBookingsResponse>, response: Response<AllBookingsResponse>) {
                if (response.isSuccessful) {
                    response.body().let {
                        callback.onNotification(it!!)
                    }
                } else
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
            }

            override fun onFailure(call: Call<AllBookingsResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }

        })
    }

    fun updateNotificationStatus(user_id: String, notify_id: String , status: String , callback: UserDataSource.UpdateNotificationStatus){
        val params : HashMap<String,String> = HashMap()
        params.let {
            it.put("user_id", user_id)
            it.put("notify_id" , notify_id)
            it.put("status" , status)
        }

        getApiService().updateNotificationStatus(params).enqueue(object : Callback<Notifications>{
            override fun onFailure(call: Call<Notifications>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }

            override fun onResponse(call: Call<Notifications>, response: Response<Notifications>) {
                if (response.isSuccessful){
                    response.body().let {
                        callback.onUpdate(it!!)
                    }
                }else{
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

        })
    }

    fun getAllAdvertisement (category_id: String , callback : UserDataSource.GetAllAdvertisement){
        val params : HashMap<String,String> = HashMap()
        params.let {
            it.put("category_id",category_id)
        }

        getApiService().getAllAdvertisement(params).enqueue(object : Callback<SubCategoriesResponse>{
            override fun onFailure(call: Call<SubCategoriesResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }

            override fun onResponse(call: Call<SubCategoriesResponse>, response: Response<SubCategoriesResponse>) {
                if(response.isSuccessful){
                    response.body().let {
                        callback.onAdvertisement(it!!)
                    }
                }else{
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

        })
    }


    fun userForgotPassword (phoneNo: String, new_password : String , callback: UserDataSource.UserForgotPassword){
        val params : HashMap<String,String> = HashMap()
        params.let {
            it.put("phone_number",phoneNo)
            it.put("new_password",new_password)

        }

        getApiService().userForgotPassword(params).enqueue(object : Callback<BaseResponse>{
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if(response.isSuccessful){
                    response.body().let {
                        callback.onChangePassword(it!!)
                    }
                }else{
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

        })
    }


}