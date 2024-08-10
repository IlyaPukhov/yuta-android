package com.yuta.domain.util

object ResponseUtil {

    fun statusToBoolean(result: String): Boolean =
        result.equals("ok", ignoreCase = true)
}