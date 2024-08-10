package com.yuta.domain.model

data class TeamCreateDto(
    val leaderId: Int,
    val name: String,
    val members: List<UserDto>
)
