package com.teknosols.a3orrsy.datamodel.model.response.wrappers


import com.teknosols.a3orrsy.datamodel.model.fan.Category
import com.teknosols.a3orrsy.datamodel.model.fan.User
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import java.io.Serializable
import java.util.ArrayList

data class CategoriesResponse(
    val data: ArrayList<Category>
): BaseResponse(),Serializable