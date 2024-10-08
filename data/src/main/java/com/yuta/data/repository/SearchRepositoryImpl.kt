package com.yuta.data.repository

import com.yuta.data.network.SearchApiService
import com.yuta.domain.model.UserDto
import com.yuta.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: SearchApiService
) : SearchRepository {

    override fun searchUsersByUsername(text: String): Flow<List<UserDto>?> = flow {
        emit(apiService.searchUsersByUsername(text).users)
    }
}
