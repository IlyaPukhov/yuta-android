package com.yuta.domain.common.model

data class Team(
    val id: Int,
    val name: String,
    val leader: UserDto,
    val members: List<UserDto>
)
