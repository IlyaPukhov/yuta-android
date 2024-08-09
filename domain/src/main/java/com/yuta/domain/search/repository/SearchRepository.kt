package com.yuta.domain.search.repository

import com.yuta.domain.common.model.UserDto
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchUsersByUsername(username: String): Flow<List<UserDto>?>
}
