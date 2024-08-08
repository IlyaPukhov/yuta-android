package com.ilyap.yuta.data.network

import com.ilyap.yuta.repository.AuthorizationApiService
import com.ilyap.yuta.repository.ProfileApiService
import com.ilyap.yuta.repository.ProjectsApiService
import com.ilyap.yuta.repository.TeamsApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiFactory {

    private lateinit var BASE_URL: String

    fun setBaseUrl(url: String) {
        BASE_URL = url
    }

    fun getBaseUrl(): String {
        return BASE_URL
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authorizationApiService: AuthorizationApiService = retrofit.create()
    val profileApiService: ProfileApiService = retrofit.create()
    val teamsApiService: TeamsApiService = retrofit.create()
    val projectApiService: ProjectsApiService = retrofit.create()
}
