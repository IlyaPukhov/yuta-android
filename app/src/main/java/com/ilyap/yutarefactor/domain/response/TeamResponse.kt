package com.ilyap.yutarefactor.domain.response

import com.ilyap.yutarefactor.domain.entity.Team

data class TeamResponse(
    val status: String,
    val error: String?,
    val team: Team?
)
