package com.teknosols.a3orrsy.datamodel.model.fan

import java.io.Serializable
import kotlin.Int

class Service: Serializable {
    var id: Int? = 0
    var service_kind: String? = ""
    var service_name: String? = ""
    var service_number: String? = ""
    var service_detail: String? = ""
    var service_price: String? = ""
    var my_status: Boolean = false
    var my_quantity: String? = "0"
}