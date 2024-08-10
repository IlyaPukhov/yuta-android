package com.yuta.domain.usecase

import com.yuta.domain.repository.AuthorizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthorizationUseCase @Inject constructor(
    private val repository: AuthorizationRepository
) {

    fun authorize(login: String, password: String): Flow<Int> {
        return repository.authorize(login, password)
            .map { it ?: -1 }
    }
}
