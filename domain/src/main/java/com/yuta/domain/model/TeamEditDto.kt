package com.yuta.domain.model

data class TeamEditDto(
    val id: Int,
    val name: String,
    val members: List<UserDto>
)
