package com.yuta.domain.profile.model

data class UserEditDto(
    val userId: Int,
    val biography: String,
    val phone: String,
    val email: String,
    val vk: String
)
