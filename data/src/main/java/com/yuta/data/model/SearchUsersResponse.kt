package com.yuta.data.model

import com.yuta.domain.model.User

data class SearchUsersResponse(
    val status: String,
    val error: String?,
    val usersDtos: List<User>?
)
