package com.yuta.domain.usecase.impl

import com.yuta.domain.exception.UserNotFoundException
import com.yuta.domain.model.User
import com.yuta.domain.model.UserEditDto
import com.yuta.domain.model.UserMiniatureUpdateDto
import com.yuta.domain.repository.ProfileRepository
import com.yuta.domain.usecase.ProfileUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val repository: ProfileRepository
) : ProfileUseCase {

    override fun getUserById(userId: Int): Flow<User> {
        return repository.getUserById(userId)
            .map { user -> user ?: throw UserNotFoundException("User with id $userId not found!") }
    }

    override fun syncUserData(userId: Int, password: String): Flow<Boolean> {
        return repository.syncUserData(userId, password)
            .map { result -> resultToBoolean(result) }
    }

    override fun editUser(userEditDto: UserEditDto): Flow<Boolean> {
        return repository.editUser(userEditDto)
            .map { result -> resultToBoolean(result) }
    }

    override fun updateUserPhoto(
        userId: Int,
        filename: String,
        photoInputStream: InputStream
    ): Flow<Boolean> {
        return repository.updateUserPhoto(userId, filename, photoInputStream)
            .map { result -> resultToBoolean(result) }
    }

    override fun updateMiniatureUserPhoto(updateMiniatureDto: UserMiniatureUpdateDto): Flow<Boolean> {
        return repository.updateMiniatureUserPhoto(updateMiniatureDto)
            .map { result -> resultToBoolean(result) }
    }

    override fun deleteUserPhoto(userId: Int): Flow<Boolean> {
        return repository.deleteUserPhoto(userId)
            .map { result -> resultToBoolean(result) }
    }

    private fun resultToBoolean(result: String): Boolean {
        return result.equals("ok", ignoreCase = true)
    }
}
