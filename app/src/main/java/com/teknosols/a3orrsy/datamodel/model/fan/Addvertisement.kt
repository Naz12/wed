package com.teknosols.a3orrsy.datamodel.model.fan

data class Addvertisement(
    var address: String?,
    var category_details: CategoryDetails?,
    var category_id: Int?,
    var city: String?,
    var destination: String?,
    var details: String?,
    var id: Int?,
    var images: List<Image?>?,
    var lat: String?,
    var lng: String?,
    var owner_name: String?,
    var p_name: String?,
    var phone: String?,
    var price: String?,
    var rating: Int?,
    var services: List<Any?>?
)