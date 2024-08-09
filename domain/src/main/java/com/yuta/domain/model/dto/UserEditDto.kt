package com.yuta.domain.model.dto

data class UserEditDto(
    val userId: Int,
    val biography: String,
    val phone: String,
    val email: String,
    val vk: String
)
