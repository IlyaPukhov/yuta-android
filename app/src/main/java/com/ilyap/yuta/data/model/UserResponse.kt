package com.ilyap.yuta.data.model

import com.ilyap.yuta.domain.model.User

data class UserResponse(
    val status: String,
    val error: String?,
    val userDto: User?
)
