package com.teknosols.a3orrsy.datamodel.source

import com.teknosols.a3orrsy.datamodel.model.fan.Notifications
import com.teknosols.a3orrsy.datamodel.model.response.ApiErrorResponse
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.*


interface UserDataSource{

    interface RegisterCallback {
        fun onRegister(user: UserResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface LoginCallback {
        fun onLogin(user: UserResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface UploadImageCallback {
        fun onUpload(image: ImageResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface UpdateUserProfileCallback {
        fun onUpdate(user: UserResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface GetCategoriesCallback {
        fun onCategory(categories: CategoriesResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface ChangePasswordCallback {
        fun onChange(baseResponse: BaseResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface GetSubCategoriesCallback {
        fun onSubCategory(subCategory: SubCategoriesResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface GetAvailableSubCategoriesCallback {
        fun onSubCategory(subCategory: SubCategoriesResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface AddBookingCallback {
        fun onBooking(baseResponse: BaseResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface GetAllBookingCallback {
        fun onGetBooking(allBookingsResponse: AllBookingsResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface GetAllUserBookingsCallback {
        fun onGetUserBooking(allBookingsResponse: AllBookingsResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface UpdateBookingStatusCallback {
        fun onUpdate(booking: BookingResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface UpdateBookingDepositCallback {
        fun onUpdate(booking: BookingResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface GetCitiesCallback {
        fun onGetting(citiesResponse: CitiesResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface GetAllFavEventsCallback {
        fun onFavEvents(allFavEventsResponse: SubCategoriesResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface GetAllUserNotifications{
        fun onNotification(allUserNotificationsResponse: AllBookingsResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface UpdateNotificationStatus{
        fun onUpdate(updateResponse: Notifications)
        fun onPayloadError(error: ApiErrorResponse)

    }

    interface GetAllAdvertisement{
        fun onAdvertisement(advertisementResponse: SubCategoriesResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface UserForgotPassword{
        fun onChangePassword(response: BaseResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

}