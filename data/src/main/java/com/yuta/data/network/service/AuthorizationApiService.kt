package com.yuta.data.network.service

import com.yuta.data.model.AuthorizationResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthorizationApiService {

    @POST("/api/authorization")
    @FormUrlEncoded
    suspend fun authorize(
        @Field("login") login: String,
        @Field("password") password: String
    ): AuthorizationResponse
}
