package com.yuta.domain.usecase.impl

import com.yuta.domain.model.UserDto
import com.yuta.domain.repository.SearchRepository
import com.yuta.domain.usecase.SearchUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchUseCaseImpl @Inject constructor(
    private val repository: SearchRepository
) : SearchUseCase {

    override fun searchUsers(username: String): Flow<List<UserDto>> {
        return repository.searchUsersByUsername(username)
            .map { it ?: emptyList() }
    }
}
