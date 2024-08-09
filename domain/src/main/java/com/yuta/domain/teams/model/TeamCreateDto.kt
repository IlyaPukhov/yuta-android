package com.yuta.domain.teams.model

import com.yuta.domain.common.model.UserDto

data class TeamCreateDto(
    val leaderId: Int,
    val name: String,
    val members: List<UserDto>
)
