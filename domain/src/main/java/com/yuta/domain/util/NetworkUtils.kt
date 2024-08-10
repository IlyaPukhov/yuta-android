package com.yuta.domain.util

object NetworkUtils {

    private lateinit var BASE_URL: String

    fun setBaseUrl(url: String) {
        BASE_URL = url
    }

    fun getBaseUrl(): String {
        return BASE_URL
    }

    fun statusToBoolean(result: String): Boolean =
        result.equals("ok", ignoreCase = true)
}