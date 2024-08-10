package com.yuta.data.network

import com.yuta.data.model.UpdateResponse
import com.yuta.data.model.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ProfileApiService {

    @GET("/api/profile")
    suspend fun getUserById(
        @Query("user_id") userId: Int
    ): UserResponse

    @POST("/api/profile")
    @FormUrlEncoded
    suspend fun syncUserData(
        @Field("user_id") userId: Int,
        @Field("password") password: String,
    ): UpdateResponse

    @POST("/api/profile")
    @FormUrlEncoded
    suspend fun editUser(
        @Field("user_id") userId: Int,
        @Field("biography") biography: String,
        @Field("phone_number") phone: String,
        @Field("e_mail") email: String,
        @Field("vk") vk: String
    ): UpdateResponse

    @POST("/api/profile")
    @Multipart
    suspend fun updateUserPhoto(
        @Field("user_id") userId: Int,
        @Part("photo") photo: MultipartBody.Part
    ): UpdateResponse

    @POST("/api/profile")
    @FormUrlEncoded
    suspend fun updateMiniatureUserPhoto(
        @Field("user_id") userId: Int,
        @Field("container_width") containerWidth: Int,
        @Field("container_height") containerHeight: Int,
        @Field("width") croppedWidth: Int,
        @Field("height") croppedHeight: Int,
        @Field("delta_x") offsetX: Int,
        @Field("delta_y") offsetY: Int
    ): UpdateResponse

    @POST("/api/profile")
    @FormUrlEncoded
    suspend fun deleteUserPhoto(
        @Field("user_id") userId: Int
    ): UpdateResponse
}
