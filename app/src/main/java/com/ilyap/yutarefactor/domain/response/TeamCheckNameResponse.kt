package com.ilyap.yutarefactor.domain.response

data class TeamCheckNameResponse(
    val status: String,
    val error: String?,
    val unique: Boolean?
)
