package com.teknosols.a3orrsy.datamodel.model.fan

import java.io.Serializable
import kotlin.Int

class SubCategory : Serializable{
    var address: String = ""
    var city: String = ""
    var category_id: Int = 0
    var details: String = ""
    var id: Int = 0
    var images: ArrayList<ProfileImage> = ArrayList()
    var services: ArrayList<Service> = ArrayList()
    var lat: String = ""
    var lng: String = ""
    var name: String = ""
    var phone: String = ""
    var price: String = ""
    var size: String = ""
    var rating: Float = 0.0f
    var event_time: String = ""
    var event_type: String = ""



    var catergory_details = Category()
    var p_name: String = ""
    var owner_Name: String = ""
    var destination : String = ""



}