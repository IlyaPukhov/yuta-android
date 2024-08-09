package com.yuta.data.model

import com.yuta.domain.model.User

data class UserResponse(
    val status: String,
    val error: String?,
    val user: User?
)
