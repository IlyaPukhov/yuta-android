package com.yuta.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthorizationRepository {

    fun authorize(login: String, password: String): Flow<Int>
}
