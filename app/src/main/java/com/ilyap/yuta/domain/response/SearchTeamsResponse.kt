package com.ilyap.yuta.domain.response

import com.ilyap.yuta.domain.entity.Team

data class SearchTeamsResponse(
    val status: String,
    val error: String?,
    val teams: List<Team>?
)
