package com.yuta.domain.usecase

import com.yuta.domain.model.UserDto
import com.yuta.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    fun searchUsers(username: String): Flow<List<UserDto>> {
        return repository.searchUsersByUsername(username)
            .map { it ?: emptyList() }
    }
}
