package com.ilyap.yutarefactor.domain.response

data class AuthorizationResponse(
    val status: String,
    val error: String?,
    val userId: Int?
)
