package com.teknosols.a3orrsy.datamodel.model.response

import java.io.Serializable

open class BaseResponse: Serializable {
    var code: Int = 0
    var message: String = ""
}