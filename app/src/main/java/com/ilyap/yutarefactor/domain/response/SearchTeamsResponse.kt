package com.ilyap.yutarefactor.domain.response

import com.ilyap.yutarefactor.domain.entity.Team

data class SearchTeamsResponse(
    val status: String,
    val error: String?,
    val teams: List<Team>?
)
