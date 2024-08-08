package com.ilyap.yuta.data.model

import com.ilyap.yuta.domain.model.Team

data class TeamsResponse(
    val status: String,
    val error: String?,
    val managedTeams: List<Team>?,
    val othersTeams: List<Team>?
)
