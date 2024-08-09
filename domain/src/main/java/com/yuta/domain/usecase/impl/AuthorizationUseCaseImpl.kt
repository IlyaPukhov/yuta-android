package com.yuta.domain.usecase.impl

import com.yuta.domain.repository.AuthorizationRepository
import com.yuta.domain.usecase.AuthorizationUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthorizationUseCaseImpl @Inject constructor(
    private val repository: AuthorizationRepository
) : AuthorizationUseCase {

    override fun authorize(login: String, password: String): Flow<Int> {
        return repository.authorize(login, password)
            .map { it ?: -1 }
    }
}
