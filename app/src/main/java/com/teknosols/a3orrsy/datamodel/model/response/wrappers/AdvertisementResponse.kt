package com.teknosols.a3orrsy.datamodel.model.response.wrappers

import com.teknosols.a3orrsy.datamodel.model.fan.Advertisement
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import java.io.Serializable

class AdvertisementResponse (
    val data : ArrayList<Advertisement>
):BaseResponse() , Serializable