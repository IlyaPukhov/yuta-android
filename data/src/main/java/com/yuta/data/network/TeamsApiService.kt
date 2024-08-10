package com.yuta.data.network

import com.yuta.data.model.SearchUsersResponse
import com.yuta.data.model.TeamCheckNameResponse
import com.yuta.data.model.TeamResponse
import com.yuta.data.model.TeamsResponse
import com.yuta.data.model.UpdateResponse
import org.json.JSONArray
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TeamsApiService {

    @GET("/api/teams")
    suspend fun getAllTeamsByUserId(
        @Query("user_id") userId: Int
    ): TeamsResponse

    @GET("/api/teams")
    suspend fun getTeamById(
        @Query("team_id") teamId: Int
    ): TeamResponse

    @GET("/api/teams")
    suspend fun searchUsersForTeam(
        @Query("user_name") username: String,
        @Query("leader_id") leaderId: Int,
        @Query("members_id") membersIds: JSONArray
    ): SearchUsersResponse

    @GET("/api/teams")
    suspend fun checkUniqueTeamName(
        @Query("team_name") teamName: String,
        @Query("team_id") teamId: Int?
    ): TeamCheckNameResponse

    @POST("/api/teams")
    @FormUrlEncoded
    suspend fun createTeam(
        @Field("leader_id") leaderId: Int,
        @Field("team_name") teamName: String,
        @Field("members_id") membersIds: JSONArray
    ): UpdateResponse

    @POST("/api/teams")
    @FormUrlEncoded
    suspend fun editTeam(
        @Field("team_id") teamId: Int,
        @Field("team_name") teamName: String,
        @Field("members_id") membersIds: JSONArray
    ): UpdateResponse

    @POST("/api/teams")
    @FormUrlEncoded
    suspend fun deleteTeam(
        @Field("team_id") teamId: Int
    ): UpdateResponse
}
