package com.yuta.domain.model.dto

import com.yuta.domain.model.User

data class TeamEditDto(
    val id: Int,
    val name: String,
    val members: List<User>
)
