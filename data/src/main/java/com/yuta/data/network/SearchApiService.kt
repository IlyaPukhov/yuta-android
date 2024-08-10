package com.yuta.data.network

import com.yuta.data.model.SearchUsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {

    @GET("/api/search")
    suspend fun searchUsersByUsername(
        @Query("user_name") username: String
    ): SearchUsersResponse
}
