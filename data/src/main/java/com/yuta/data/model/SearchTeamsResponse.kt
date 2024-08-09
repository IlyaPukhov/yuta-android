package com.yuta.data.model

import com.yuta.domain.model.Team

data class SearchTeamsResponse(
    val status: String,
    val error: String?,
    val teams: List<Team>?
)
