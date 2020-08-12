package com.teknosols.a3orrsy.datamodel.model.response.wrappers


import com.teknosols.a3orrsy.datamodel.model.fan.ProfileImage
import com.teknosols.a3orrsy.datamodel.model.fan.User
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import java.io.Serializable
import java.util.ArrayList

data class ImageResponse(
    val data: ProfileImage
): BaseResponse(),Serializable