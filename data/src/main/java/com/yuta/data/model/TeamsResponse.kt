package com.yuta.data.model

import com.yuta.domain.common.model.Team

data class TeamsResponse(
    val status: String,
    val error: String?,
    val managedTeams: List<Team>?,
    val othersTeams: List<Team>?
)
