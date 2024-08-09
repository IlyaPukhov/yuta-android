package com.yuta.domain.teams.model

import com.yuta.domain.common.model.UserDto

data class TeamEditDto(
    val id: Int,
    val name: String,
    val members: List<UserDto>
)
