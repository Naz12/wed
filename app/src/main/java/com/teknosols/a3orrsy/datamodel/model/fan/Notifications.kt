package com.teknosols.a3orrsy.datamodel.model.fan

import java.io.Serializable
import kotlin.Int

data class Notifications(
    var booking_id: String? = "",
    var created_at: String? = "",
    var id: Int? = 0,
    var is_seen: Int? = 0,
    var message: String? = "",
    var updated_at: String? = "",
    var user_id: String? = ""
): Serializable