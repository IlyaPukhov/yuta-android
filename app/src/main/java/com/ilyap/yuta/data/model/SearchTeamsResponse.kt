package com.ilyap.yuta.data.model

import com.ilyap.yuta.domain.model.Team

data class SearchTeamsResponse(
    val status: String,
    val error: String?,
    val teams: List<Team>?
)
