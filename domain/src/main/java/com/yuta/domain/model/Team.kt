package com.yuta.domain.model

data class Team(
    val id: Int,
    val name: String,
    val leader: User,
    val members: List<User>
)
