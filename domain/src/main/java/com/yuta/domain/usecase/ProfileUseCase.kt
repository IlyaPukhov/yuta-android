package com.yuta.domain.usecase

import com.yuta.domain.model.User
import com.yuta.domain.model.UserEditDto
import com.yuta.domain.model.UserMiniatureUpdateDto
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface ProfileUseCase {

    fun getUserById(userId: Int): Flow<User>

    fun syncUserData(userId: Int, password: String): Flow<Boolean>

    fun editUser(userEditDto: UserEditDto): Flow<Boolean>

    fun updateUserPhoto(userId: Int, filename: String, photoInputStream: InputStream): Flow<Boolean>

    fun updateMiniatureUserPhoto(updateMiniatureDto: UserMiniatureUpdateDto): Flow<Boolean>

    fun deleteUserPhoto(userId: Int): Flow<Boolean>
}
