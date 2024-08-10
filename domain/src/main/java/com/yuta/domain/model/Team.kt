package com.yuta.domain.model

data class Team(
    val id: Int,
    val name: String,
    val leader: UserDto,
    val members: List<UserDto>
)
