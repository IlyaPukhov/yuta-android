package com.ilyap.yutarefactor.domain.response

import com.ilyap.yutarefactor.domain.entity.Team

data class TeamsResponse(
    val status: String,
    val error: String?,
    val managedTeams: List<Team>?,
    val othersTeams: List<Team>?
)
