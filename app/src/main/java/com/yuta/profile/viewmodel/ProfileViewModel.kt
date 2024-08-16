package com.yuta.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.model.User
import com.yuta.domain.model.UserEditDto
import com.yuta.domain.model.UserMiniatureUpdateDto
import com.yuta.domain.usecase.ProfileUseCase
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val useCase: ProfileUseCase
) : ViewModel() {

    fun getProfile(userId: Int): Flow<User> {
        return useCase.getUserById(userId)
    }
}
