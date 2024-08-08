package com.ilyap.yuta.domain.response

import com.ilyap.yuta.domain.entity.UserUpdateDto

data class SearchUsersResponse(
    val status: String,
    val error: String?,
    val usersDtos: List<UserUpdateDto>?
)
