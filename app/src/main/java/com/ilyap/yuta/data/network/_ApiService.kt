package com.ilyap.yuta.data.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface _ApiService {

    @GET("{path}")
    fun getRequest(
        @Path("path") path: String,
        @QueryMap params: Map<String, String>
    ): Call<String>

    @POST("{path}")
    @FormUrlEncoded
    fun postRequest(
        @Path("path") path: String,
        @FieldMap params: Map<String, String>
    ): Call<String>

    @Multipart
    @POST("{path}")
    fun postFormDataRequest(
        @Path("path") path: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part
    ): Call<String>
}
