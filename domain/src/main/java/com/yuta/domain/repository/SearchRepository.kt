package com.yuta.domain.repository

import com.yuta.domain.model.UserDto
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchUsersByUsername(username: String): Flow<List<UserDto>?>
}
