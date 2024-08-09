package com.yuta.domain.model.dto

import com.yuta.domain.model.Team

data class TeamsDto(
    val managedTeams: List<Team>?,
    val othersTeams: List<Team>?
)
