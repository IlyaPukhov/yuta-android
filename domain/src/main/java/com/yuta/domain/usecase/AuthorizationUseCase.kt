package com.yuta.domain.usecase

import kotlinx.coroutines.flow.Flow

interface AuthorizationUseCase {

    fun authorize(login: String, password: String): Flow<Int>
}
