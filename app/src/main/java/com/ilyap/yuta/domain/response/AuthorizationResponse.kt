package com.ilyap.yuta.domain.response

data class AuthorizationResponse(
    val status: String,
    val error: String?,
    val userId: Int?
)
