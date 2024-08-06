package com.ilyap.yutarefactor.domain.response

import com.ilyap.yutarefactor.domain.entity.UserUpdateDto

data class SearchUsersResponse(
    val status: String,
    val error: String?,
    val usersDtos: List<UserUpdateDto>?
)
