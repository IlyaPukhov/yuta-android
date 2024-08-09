package com.yuta.data.network

import com.yuta.data.model.ProjectResponse
import com.yuta.data.model.ProjectsResponse
import com.yuta.data.model.SearchTeamsResponse
import com.yuta.data.model.UpdateResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ProjectsApiService {

    @GET("/api/projects")
    suspend fun getAllProjectsByUserId(
        @Query("user_id") userId: Int
    ): ProjectsResponse

    @GET("/api/projects")
    suspend fun getProjectById(
        @Query("project_id") projectId: Int
    ): ProjectResponse

    @GET("/api/projects")
    suspend fun searchTeamsForProject(
        @Query("team_name") name: String,
        @Query("leader_id") leaderId: Int,
        @Query("project_team_id") teamId: Int?
    ): SearchTeamsResponse

    @POST("/api/projects")
    @Multipart
    suspend fun createProject(
        @Field("manager_id") managerId: Int,
        @Field("project_name") name: String,
        @Field("project_description") description: String,
        @Field("project_deadline") deadline: String,
        @Field("project_team_id") teamId: Int?,
        @Part("project_technical_task") technicalTask: MultipartBody.Part?
    ): UpdateResponse

    @POST("/api/projects")
    @Multipart
    suspend fun editProject(
        @Field("project_id") projectId: Int,
        @Field("project_name") name: String,
        @Field("project_description") description: String,
        @Field("project_deadline") deadline: String,
        @Field("project_team_id") teamId: Int?,
        @Field("project_status") status: String,
        @Part("project_technical_task") technicalTask: MultipartBody.Part?
    ): UpdateResponse

    @POST("/api/projects")
    @FormUrlEncoded
    suspend fun deleteProject(
        @Field("project_id") projectId: Int
    ): UpdateResponse
}
