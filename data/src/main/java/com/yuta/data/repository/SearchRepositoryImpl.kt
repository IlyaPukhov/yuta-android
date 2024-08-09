package com.yuta.data.repository

import com.yuta.data.network.SearchApiService
import com.yuta.domain.model.User
import com.yuta.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: SearchApiService
) : SearchRepository {

    override fun searchUsersByUsername(username: String): Flow<List<User>?> = flow {
        emit(apiService.searchUsersByUsername(username).users)
    }
}
