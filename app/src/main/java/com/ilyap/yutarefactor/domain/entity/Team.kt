package com.ilyap.yutarefactor.domain.entity

data class Team(
    val id: Int,
    val name: String,
    val leader: UserUpdateDto,
    val members: List<UserUpdateDto>
)
