package com.yuta.data.model

import com.yuta.domain.common.model.UserDto

data class SearchUsersResponse(
    val status: String,
    val error: String?,
    val users: List<UserDto>?
)
