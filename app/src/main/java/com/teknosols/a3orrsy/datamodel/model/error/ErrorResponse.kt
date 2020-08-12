package com.teknosols.a3orrsy.datamodel.model.error

import java.io.Serializable

class ErrorResponse(var errorMessage: String, var errorCode: Int) : Serializable {

}