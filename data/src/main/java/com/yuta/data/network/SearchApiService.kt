package com.yuta.data.network

import com.yuta.data.model.SearchUsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {

    @GET("/api/search")
    suspend fun getUsersByUsername(
        @Query("user_name") username: String
    ): SearchUsersResponse
}
