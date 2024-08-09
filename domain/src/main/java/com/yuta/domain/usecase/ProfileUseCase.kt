package com.yuta.domain.usecase

import com.yuta.domain.model.User
import com.yuta.domain.model.UserEditDto
import com.yuta.domain.model.UserMiniatureUpdateDto
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface ProfileUseCase {

    fun getUserById(userId: Int): Flow<User>

    fun syncUserData(userId: Int, password: String): Flow<Void>

    fun editUser(userEditDto: UserEditDto): Flow<Void>

    fun updateUserPhoto(userId: Int, filename: String, photoInputStream: InputStream): Flow<Void>

    fun updateMiniatureUserPhoto(updateMiniatureDto: UserMiniatureUpdateDto): Flow<Void>

    fun deleteUserPhoto(userId: Int): Flow<Void>
}
