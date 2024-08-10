package com.yuta.data.repository

import com.yuta.data.network.ProfileApiService
import com.yuta.domain.model.User
import com.yuta.domain.model.UserEditDto
import com.yuta.domain.model.UserMiniatureUpdateDto
import com.yuta.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ProfileApiService
) : ProfileRepository {

    override fun getUserById(userId: Int): Flow<User?> = flow {
        emit(apiService.getUserById(userId).user)
    }

    override fun syncUserData(userId: Int, password: String): Flow<String> = flow {
        emit(apiService.syncUserData(userId, password).status)
    }

    override fun editUser(userEditDto: UserEditDto): Flow<String> = flow {
        emit(
            apiService.editUser(
                userId = userEditDto.userId,
                biography = userEditDto.biography,
                phone = userEditDto.phone,
                email = userEditDto.email,
                vk = userEditDto.vk
            ).status
        )
    }

    override fun updateUserPhoto(
        userId: Int,
        filename: String,
        photoInputStream: InputStream
    ): Flow<String> = flow {
        val photoPart = photoInputStream.let {
            val requestBody = it.readBytes().toRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("photo", filename, requestBody)
        }

        emit(apiService.updateUserPhoto(userId, photoPart).status)
    }

    override fun updateMiniatureUserPhoto(updateDto: UserMiniatureUpdateDto): Flow<String> = flow {
        emit(
            apiService.updateMiniatureUserPhoto(
                userId = updateDto.userId,
                containerWidth = updateDto.containerWidth,
                containerHeight = updateDto.containerHeight,
                croppedWidth = updateDto.croppedWidth,
                croppedHeight = updateDto.croppedHeight,
                offsetX = updateDto.offsetX,
                offsetY = updateDto.offsetY
            ).status
        )
    }

    override fun deleteUserPhoto(userId: Int): Flow<String> = flow {
        emit(apiService.deleteUserPhoto(userId).status)
    }
}