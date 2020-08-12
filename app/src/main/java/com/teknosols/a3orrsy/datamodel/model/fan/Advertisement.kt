package com.teknosols.a3orrsy.datamodel.model.fan

import java.io.Serializable
import kotlin.Int

class Advertisement : Serializable {
    var address: String = ""
    var city: String = ""
    var details: String = ""
    var id: kotlin.Int = 0
    var category_id: Int? = 0
    var catergory_details = Category()
    var images: ArrayList<Image>? = ArrayList()
    var services: ArrayList<Service>? = ArrayList()
    var lat: String = ""
    var lng: String = ""
    var p_name: String = ""
    var owner_Name: String = ""
    var destination : String = ""
    var phone: String = ""
    var price: String = ""
    var rating: Float = 0.0f

}