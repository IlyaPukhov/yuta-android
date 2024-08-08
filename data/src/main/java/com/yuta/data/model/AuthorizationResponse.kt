package com.yuta.data.model

data class AuthorizationResponse(
    val status: String,
    val error: String?,
    val userId: Int?
)
