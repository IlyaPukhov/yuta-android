package com.ilyap.yutarefactor.domain.response

import com.ilyap.yutarefactor.domain.entity.UserUpdateDto

data class UserResponse(
    val status: String,
    val error: String?,
    val userDto: UserUpdateDto?
)
