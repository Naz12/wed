package com.teknosols.a3orrsy.datamodel.model.fan

import java.io.Serializable
import kotlin.Int

class Booking: Serializable {
    var booking_status: String? = ""
    var booking_time: String? = ""
    var category_details: Category? = Category()
    var category_id: Int? = 0
    var current_date_time: String? = ""
    var date_from: String? = ""
    var date_to: String? = ""
    var event_details: SubCategory? = SubCategory()
    var event_id: Int? = 0
    var id: Int? = 0
    var phone: String? = ""
    var price: String? = ""
    var ref_no: String? = ""
    var type: String? = ""
    var user_details: User? = User()
    var user_id: Int = 0
    var deposit_history: ArrayList<DepositHistory>? = ArrayList()
    var services: ArrayList<BookingServices> = ArrayList()
    var notifications: ArrayList<Notifications> = ArrayList()

}