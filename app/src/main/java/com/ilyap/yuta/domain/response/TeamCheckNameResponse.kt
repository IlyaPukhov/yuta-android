package com.ilyap.yuta.domain.response

data class TeamCheckNameResponse(
    val status: String,
    val error: String?,
    val unique: Boolean?
)
