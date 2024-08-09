package com.yuta.domain.usecase

import com.yuta.domain.model.UserDto
import kotlinx.coroutines.flow.Flow

interface SearchUseCase {

    fun searchUsers(username: String): Flow<List<UserDto>>
}
