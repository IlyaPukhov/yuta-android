package com.yuta.domain.usecase

import com.yuta.domain.exception.NotFoundException
import com.yuta.domain.model.User
import com.yuta.domain.model.UserEditDto
import com.yuta.domain.model.UserMiniatureUpdateDto
import com.yuta.domain.repository.ProfileRepository
import com.yuta.domain.util.NetworkUtils.statusToBoolean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {

    fun getUserById(userId: Int): Flow<User> {
        return repository.getUserById(userId)
            .map { it ?: throw NotFoundException("User with id $userId not found!") }
    }

    fun syncUserData(userId: Int, password: String): Flow<Boolean> {
        return repository.syncUserData(userId, password)
            .map { statusToBoolean(it) }
    }

    fun editUser(userEditDto: UserEditDto): Flow<Boolean> {
        return repository.editUser(userEditDto)
            .map { statusToBoolean(it) }
    }

    fun updateUserPhoto(
        userId: Int,
        filename: String,
        photoInputStream: InputStream
    ): Flow<Boolean> {
        return repository.updateUserPhoto(userId, filename, photoInputStream)
            .map { statusToBoolean(it) }
    }

    fun updateMiniatureUserPhoto(updateMiniatureDto: UserMiniatureUpdateDto): Flow<Boolean> {
        return repository.updateMiniatureUserPhoto(updateMiniatureDto)
            .map { statusToBoolean(it) }
    }

    fun deleteUserPhoto(userId: Int): Flow<Boolean> {
        return repository.deleteUserPhoto(userId)
            .map { statusToBoolean(it) }
    }
}
