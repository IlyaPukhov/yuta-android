package com.ilyap.yuta.domain.response

import com.ilyap.yuta.domain.entity.Team

data class TeamsResponse(
    val status: String,
    val error: String?,
    val managedTeams: List<Team>?,
    val othersTeams: List<Team>?
)
