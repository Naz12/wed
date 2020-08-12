package com.teknosols.a3orrsy.other.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.teknosols.a3orrsy.datamodel.model.fan.User
import com.teknosols.a3orrsy.datamodel.source.Remote.RetrofitClientInstance
import com.teknosols.a3orrsy.view.activites.LandingActivity


class SessionManager {
    var context: Context? = null
    var pref: SharedPreferences

    constructor(context: Context) {
        this.context = context
        pref = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(): Boolean {
        return !getAuthenticationToken().isNullOrEmpty();
    }

      fun setAuthenticationToken(token: String) {
        with(pref.edit()) {
            putString(TOKEN, token)
            commit()
        }
        RetrofitClientInstance.getInstance(context!!)!!.initRetrofit()
    }

    fun getAuthenticationToken(): String {
        return pref.getString(TOKEN, "")!!
    }

    fun setUserId(id: String) {
        with(pref.edit()) {
            putString(KEY_USERID, id)
            commit()
        }
    }

    fun getUserId(): String {
        return pref.getString(KEY_USERID, "")
    }

    fun setLanguage(language: String) {
        with(pref.edit()) {
            putString(MY_LANGUAGE, language)
            commit()
        }
    }

    fun getLanguage(): String {
        return pref.getString(MY_LANGUAGE, "")
    }

    fun setLoginAs(loginAs: String) {
        with(pref.edit()) {
            putString(KEY_LOGINAS, loginAs)
            commit()
        }
    }

    fun getLoginAs(): String {
        return pref.getString(KEY_LOGINAS, "")
    }

    fun setFirstName(firstName: String?) {
        with(pref.edit()) {
            putString(KEY_NAME, firstName)
            commit()
        }
    }

    fun getFirstName(): String {
        return pref.getString(KEY_NAME, "")!!
    }

    fun setEmail(email: String?) {
        with(pref.edit()) {
            putString(KEY_EMAIL, email)
            commit()
        }
    }

    fun getEmail(): String {
        return pref.getString(KEY_EMAIL, "")!!
    }

    fun setPhone(phone: String?) {
        with(pref.edit()) {
            putString(KEY_PHONENO, phone)
            commit()
        }
    }

    fun getPhone(): String {
        return pref.getString(KEY_PHONENO, "")
    }

    fun setProfileImage(profileImage: String) {
        with(pref.edit()) {
            putString(KEY_USERIMAGE, profileImage)
            commit()
        }
    }

    fun getProfileImage(): String {
        return pref.getString(KEY_USERIMAGE, "")
    }


    fun setBirthday(birthday: String) {
        with(pref.edit()) {
            putString(KEY_BIRTHDAY, birthday)
            commit()
        }
    }

    fun setAddressLine1(addresLine1: String?) {
        with(pref.edit()) {
            putString(ADDRESS_LINE_1, addresLine1)
            commit()
        }
    }


    fun getBirthday(): String {
        return pref.getString(KEY_BIRTHDAY, "")
    }

    fun setCnic(cnic: String) {
        with(pref.edit()) {
            putString(KEY_CNIC, cnic)
            commit()
        }
    }

    fun getAddressLine1(): String {
        return pref.getString(ADDRESS_LINE_1, "")!!
    }

    fun setAddressLine2(addresLine2: String) {
        with(pref.edit()) {
            putString(ADDRESS_LINE_2, addresLine2)
            commit()
        }
    }

    fun getCnic(): String {
        return pref.getString(KEY_CNIC, "")
    }

    fun setCnicFrontImage(cnicFrontImage: String) {
        with(pref.edit()) {
            putString(KEY_CNIC_FRONT_IMAGE, cnicFrontImage)
            commit()
        }
    }

    fun getAddressLine2(): String {
        return pref.getString(ADDRESS_LINE_2, "")
    }

    fun setCountry(country: String?) {
        with(pref.edit()) {
            putString(COUNTRY, country)
            commit()
        }
    }

    fun getCnicFrontImage(): String {
        return pref.getString(KEY_CNIC_FRONT_IMAGE, "")
    }

    fun setCnicBackImage(cnicBackImage: String) {
        with(pref.edit()) {
            putString(KEY_CNIC_BACK_IMAGE, cnicBackImage)
            commit()
        }
    }

    fun getCnicBackImage(): String {
        return pref.getString(KEY_CNIC_BACK_IMAGE, "")
    }

    fun getCountry(): String {
        return pref.getString(COUNTRY, "")!!
    }

    fun setCity(city: String?) {
        with(pref.edit()) {
            putString(CITY, city)
            commit()
        }
    }


    fun getCity(): String {
        return pref.getString(CITY, "")
    }

    fun setStateRegionProvince(str: String?) {
        with(pref.edit()) {
            putString(STATE_REGION_PROVINCE, str)
            commit()
        }
    }

    fun getStateRegionProvince(): String {
        return pref.getString(STATE_REGION_PROVINCE, "")!!
    }

    fun setZip(zip: String?) {
        with(pref.edit()) {
            putString(ZIP, zip)
            commit()
        }
    }

    fun getZip(): String {
        return pref.getString(ZIP, "")
    }

    fun setDeviceId(deviceId: String) {
        with(pref.edit()) {
            putString(DEVICE_ID, deviceId)
            commit()
        }
    }

    fun getDeviceId(): String {
        return pref.getString(DEVICE_ID, "")!!
    }

    fun setDeviceToken(deviceId: String) {
        with(pref.edit()) {
            putString(DEVICE_TOKEN, deviceId)
            commit()
        }
    }

    fun getDeviceToken(): String {
        return pref.getString(DEVICE_TOKEN, "")
    }

    fun logout() {
        setAuthenticationToken("")
        setUserId("")
        setFirstName("")
        setEmail("")
        setPhone("")
        setProfileImage("")
        setBirthday("")
        setAddressLine1("")
        setCity("")
        setCountry("")
        setLoginAs("")
//        pref.edit().clear().apply()
    }

    fun redirectToLogin(context: Context, message: String) {
        val intent = Intent(context, LandingActivity::class.java)
        intent.putExtra(LandingActivity.SKIP_SPLASH, true)
        intent.putExtra(LandingActivity.START_UP_MESSAGe, message)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun setUser(u: User) {
        setAuthenticationToken(u.token ?: "")
        setUserId(u.id.toString())
        setFirstName(u.name)
        setEmail(u.email)
        setPhone(u.phone)
        setProfileImage(u.avatar)
        setBirthday(u.birthday)
        setAddressLine1(u.address)
        setCity(u.city)
        setCountry(u.country)
        setLoginAs(u.login_as)
    }

    fun getLocale(): String {
        return "en"
    }

    companion object {
        val PREF_NAME: String = "app_pref"
        val TOKEN: String = "authentication_token"
        val USER_ID: String = "user_id"
        val FIRST_NAME: String = "first_name"
        val LAST_NAME: String = "last_name"
        val EMAIL: String = "email"
        val PHONE: String = "phone"
        val GENDER: String = "gender"
        val AGE: String = "age"
        val PROFILE_IMAGE: String = "profile_image"
        val INVISIBLE: String = "invisible"

        val ADDRESS_LINE_1: String = "address_line_1"
        val ADDRESS_LINE_2: String = "address_line_2"
        val COUNTRY: String = "country"
        val CITY: String = "city"
        val STATE_REGION_PROVINCE = "state/region/province"
        val ZIP = "ZIP"

        val DEVICE_ID = "deviceid"
        val DEVICE_TOKEN = "devicetoken"
        val CART_ORDER_ID = "cartorderid"
        val PIMP_ID = "pimp_id"
        val LOCALE = "locale"
        val BEACONS = "beacons"


        val KEY_EMAIL = "email"
        val KEY_NAME = "vehicle_type"
        val KEY_USERID = "userid"
        val KEY_LOGINAS = "loginAs"
        val KEY_USERIMAGE = "userimage"
        val KEY_PHONENO = "phoneno"
        val MY_LANGUAGE = "language"

        val KEY_BIRTHDAY = "birthday"
        val KEY_CNIC = "cnic"
        val KEY_CNIC_FRONT_IMAGE = "cnic_front_image"
        val KEY_CNIC_BACK_IMAGE = "cnic_back_image"


        val KEY_USERNAME = "username"
        val KEY_GCM_ID = "gcmId"

        val KEY_COUNTRYCODE = "countrycode"
        val KEY_REFERAL_CODE = "referalcode"
        val KEY_CATEGORY = "category"
        val KEY_CATEGORY_ID = "categoryId"
        val KEY_Language_code = "language_code"
        val KEY_Language = "language"

        val KEY_COUPON_CODE = "coupon"
        val KEY_WALLET_AMOUNT = "walletAmount"

        val KEY_XMPP_USERID = "xmppUserId"
        val KEY_XMPP_SEC_KEY = "xmppSecKey"

        val KEY_HOST_URL = "xmpphostUrl"
        val KEY_HOST_NAME = "xmpphostName"
        val KEY_ID_NAME = "Id_Name"
        val KEY_VEHICLE_BitMap_IMAGE = "bitmap"
        val KEY_ABOUT_US = "about_us"
        val KEY_USER_IMAGE = "user_image"

        val KEY_APP_STATUS = "appStatus"

    }

}
