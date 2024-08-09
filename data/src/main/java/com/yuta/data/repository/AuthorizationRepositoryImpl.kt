package com.yuta.data.repository

import com.yuta.data.network.AuthorizationApiService
import com.yuta.domain.authorization.repository.AuthorizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthorizationRepositoryImpl @Inject constructor(
    private val apiService: AuthorizationApiService
) : AuthorizationRepository {

    override fun authorize(login: String, password: String): Flow<Int?> = flow {
        emit(apiService.authorize(login, password).userId)
    }
}
