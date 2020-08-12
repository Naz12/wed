package com.teknosols.a3orrsy.datamodel.source.Remote

import com.teknosols.a3orrsy.datamodel.model.fan.Notifications
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*
import retrofit2.http.POST


interface ApiService {

    @Multipart
    @POST("register_user")
    fun register(
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("birthday") birthday: RequestBody,
        @Part("address") address: RequestBody,
        @Part("city") city: RequestBody,
        @Part("country") country: RequestBody,
        @Part("token") token: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<UserResponse>

    @POST("register_user")
    fun registerWithoutImage(@Body body: HashMap<String, String>): Call<UserResponse>

    @POST("login_user")
    fun userLogin(@Body body: HashMap<String, String>): Call<UserResponse>

    @Multipart
    @POST("update_profile_image")
    fun uploadImage(@Part("user_id") name: RequestBody, @Part file: MultipartBody.Part): Call<ImageResponse>

    @POST("update_user_profile")
    fun updateUserProfile(@Body body: HashMap<String, String>): Call<UserResponse>

    @GET("get_categories")
    fun getCategories(): Call<CategoriesResponse>

    @POST("update_user_password")
    fun changePassword(@Body body: HashMap<String, String>): Call<BaseResponse>

    @POST("get_all_events")
    fun getSubCategories(@Body body: HashMap<String, String>): Call<SubCategoriesResponse>

    @POST("get_available_events")
    fun getAvailableSubCategories(@Body body: HashMap<String, String>): Call<SubCategoriesResponse>

    @POST("add_booking")
    fun addBooking(@Body body: HashMap<String, String>): Call<BaseResponse>

    @GET("get_all_bookings")
    fun getAllBookings(): Call<AllBookingsResponse>

    @POST("get_all_user_bookings")
    fun getAllUserBookings(@Body body : HashMap<String , String>) : Call<AllBookingsResponse>

    @POST("update_booking_status")
    fun updateBookingStatus(@Body body: HashMap<String, String>): Call<BookingResponse>

    @POST("update_booking_deposit")
    fun updateBookingDeposit(@Body body: HashMap<String, String>): Call<BookingResponse>

    @GET("get_cities")
    fun getCities(): Call<CitiesResponse>

    @GET("get_all_fav_events")
    fun getAllFavEvents() : Call<SubCategoriesResponse>

    @POST("get_all_user_notifications")
    fun getAllUserNotifications(@Body body: HashMap<String,String>): Call<AllBookingsResponse>

    @POST("update_notification_status")
    fun updateNotificationStatus(@Body body: HashMap<String,String>): Call<Notifications>

    @POST("get_all_advertisements")
    fun getAllAdvertisement(@Body body : HashMap<String,String>) : Call<SubCategoriesResponse>

    @POST("user_forgot_password")
    fun userForgotPassword(@Body body : HashMap<String,String>) : Call<BaseResponse>

}