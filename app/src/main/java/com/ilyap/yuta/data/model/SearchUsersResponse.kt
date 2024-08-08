package com.ilyap.yuta.data.model

import com.ilyap.yuta.domain.model.User

data class SearchUsersResponse(
    val status: String,
    val error: String?,
    val usersDtos: List<User>?
)
