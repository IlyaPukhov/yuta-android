package com.yuta.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.model.User
import com.yuta.domain.usecase.ProfileUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val useCase: ProfileUseCase
) : ViewModel() {

    fun getProfile(userId: Int): Flow<User> {
        return useCase.getUserById(userId)
    }
}
