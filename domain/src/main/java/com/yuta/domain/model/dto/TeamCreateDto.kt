package com.yuta.domain.model.dto

import com.yuta.domain.model.User

data class TeamCreateDto(
    val leaderId: Int,
    val name: String,
    val members: List<User>
)
