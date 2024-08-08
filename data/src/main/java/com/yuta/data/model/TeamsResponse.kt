package com.yuta.data.model

import com.yuta.domain.model.Team

data class TeamsResponse(
    val status: String,
    val error: String?,
    val managedTeams: List<Team>?,
    val othersTeams: List<Team>?
)
