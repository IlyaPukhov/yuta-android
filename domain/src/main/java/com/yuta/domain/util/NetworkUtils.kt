package com.yuta.domain.util

object NetworkUtils {

    lateinit var BASE_URL: String

    fun statusToBoolean(result: String): Boolean =
        result.equals("ok", ignoreCase = true)
}