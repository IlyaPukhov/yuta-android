package com.yuta.domain.teams.model

import com.yuta.domain.common.model.Team

data class TeamsDto(
    val managedTeams: List<Team>?,
    val othersTeams: List<Team>?
)
