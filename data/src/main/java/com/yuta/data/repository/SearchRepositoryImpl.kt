package com.yuta.data.repository

import com.yuta.data.network.SearchApiService
import com.yuta.domain.common.model.UserDto
import com.yuta.domain.search.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: SearchApiService
) : SearchRepository {

    override fun searchUsersByUsername(username: String): Flow<List<UserDto>?> = flow {
        emit(apiService.searchUsersByUsername(username).users)
    }
}
