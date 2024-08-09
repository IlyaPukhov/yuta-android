package com.yuta.data.network

import com.yuta.domain.repository.AuthorizationApiService
import com.yuta.domain.repository.ProfileApiService
import com.yuta.domain.repository.ProjectsApiService
import com.yuta.domain.repository.TeamsApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
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
