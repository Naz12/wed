package com.teknosols.a3orrsy.other.util

import com.google.gson.Gson
import com.teknosols.a3orrsy.datamodel.model.fan.Booking

class Parser{

    companion object{
        fun toBooking(json: String): Booking{
            var booking = Booking()
            try {
                booking = Gson().fromJson(json, Booking::class.java)
                return booking
            }catch (ex: Exception){
                ex.printStackTrace()
            }
            return booking
        }
    }
}