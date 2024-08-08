package com.ilyap.yuta.domain.response

import com.ilyap.yuta.domain.entity.UserUpdateDto

data class UserResponse(
    val status: String,
    val error: String?,
    val userDto: UserUpdateDto?
)
