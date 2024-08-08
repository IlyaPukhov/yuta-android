package com.ilyap.yuta.domain.response

import com.ilyap.yuta.domain.entity.Team

data class TeamResponse(
    val status: String,
    val error: String?,
    val team: Team?
)
