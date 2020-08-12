package com.teknosols.a3orrsy.datamodel.model.response.wrappers


import com.teknosols.a3orrsy.datamodel.model.fan.City
import com.teknosols.a3orrsy.datamodel.model.fan.User
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import java.io.Serializable
import java.util.ArrayList

data class CitiesResponse(
    val data: ArrayList<City>
): BaseResponse(),Serializable