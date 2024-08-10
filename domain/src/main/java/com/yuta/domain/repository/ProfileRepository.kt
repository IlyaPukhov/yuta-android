package com.yuta.domain.repository

import com.yuta.domain.model.User
import com.yuta.domain.model.UserEditDto
import com.yuta.domain.model.UserMiniatureUpdateDto
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface ProfileRepository {

    fun getUserById(userId: Int): Flow<User?>

    fun syncUserData(userId: Int, password: String): Flow<String>

    fun editUser(userEditDto: UserEditDto): Flow<String>

    fun updateUserPhoto(userId: Int, filename: String, photoInputStream: InputStream): Flow<String>

    fun updateMiniatureUserPhoto(updateDto: UserMiniatureUpdateDto): Flow<String>

    fun deleteUserPhoto(userId: Int): Flow<String>
}
