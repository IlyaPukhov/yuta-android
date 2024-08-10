package com.yuta.authorization.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.usecase.AuthorizationUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(
    private val useCase: AuthorizationUseCase
) : ViewModel() {

    fun auth(login: String, password: String): Flow<Int> {
        return useCase.authorize(login, password)
    }
}
