package com.teknosols.a3orrsy.datamodel.model.fan

import java.io.Serializable
import kotlin.Int

class BookingServices : Serializable{
    var booking_id: Int? = 0
    var bs_detail: String? = ""
    var bs_kind: String? = ""
    var bs_name: String? = ""
    var bs_number: String? = ""
    var bs_price: String? = ""
    var category_id: Int? = 0
    var created_at: String? = ""
    var event_id: Int? = 0
    var id: Int?  = 0
    var service_id: Int? = 0
    var updated_at: String?  = ""
    var user_id: Int? = 0
}