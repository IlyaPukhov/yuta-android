package com.yuta.app.utils

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object JsonUtils {

    private val gson: Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    fun <T> parse(jsonString: String, clazz: Class<T>): T {
        return gson.fromJson(jsonString, clazz)
    }
}
