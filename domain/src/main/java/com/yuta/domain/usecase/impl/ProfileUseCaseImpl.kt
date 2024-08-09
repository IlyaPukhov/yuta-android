package com.yuta.domain.usecase.impl

import com.yuta.domain.model.User
import com.yuta.domain.model.UserEditDto
import com.yuta.domain.model.UserMiniatureUpdateDto
import com.yuta.domain.repository.ProfileRepository
import com.yuta.domain.usecase.ProfileUseCase
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val repository: ProfileRepository
) : ProfileUseCase {

    override fun getUserById(userId: Int): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun syncUserData(
        userId: Int,
        password: String
    ): Flow<Void> {
        TODO("Not yet implemented")
    }

    override fun editUser(userEditDto: UserEditDto): Flow<Void> {
        TODO("Not yet implemented")
    }

    override fun updateUserPhoto(
        userId: Int,
        filename: String,
        photoInputStream: InputStream
    ): Flow<Void> {
        TODO("Not yet implemented")
    }

    override fun updateMiniatureUserPhoto(updateMiniatureDto: UserMiniatureUpdateDto): Flow<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteUserPhoto(userId: Int): Flow<Void> {
        TODO("Not yet implemented")
    }
}
