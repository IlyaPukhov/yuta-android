package com.yuta.data.model

import com.yuta.domain.model.Team

data class TeamResponse(
    val status: String,
    val error: String?,
    val team: Team?
)
