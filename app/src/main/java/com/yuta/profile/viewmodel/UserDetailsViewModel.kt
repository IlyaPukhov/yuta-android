package com.yuta.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.model.UserEditDto
import com.yuta.domain.model.UserMiniatureUpdateDto
import com.yuta.domain.usecase.ProfileUseCase
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import javax.inject.Inject

class UserDetailsViewModel @Inject constructor(
    private val useCase: ProfileUseCase
) : ViewModel() {

    fun syncProfile(userId: Int, password: String): Flow<Boolean> {
        return useCase.syncUserData(userId, password)
    }

    fun editDetails(userId: Int, biography: String?, phone: String?, email: String?, vk: String?): Flow<Boolean> {
        return useCase.editUser(
            UserEditDto(
                userId = userId,
                biography = biography ?: "",
                phone = phone ?: "",
                email = email ?: "",
                vk = vk ?: ""
            )
        )
    }

    fun uploadPhoto(userId: Int, filename: String, photo: InputStream): Flow<Boolean> {
        return useCase.updateUserPhoto(userId, filename, photo)
    }

    fun updateMiniature(
        userId: Int, ivWidth: Int, ivHeight: Int,
        croppedWidth: Int, croppedHeight: Int,
        offsetX: Int, offsetY: Int
    ): Flow<Boolean> {
        return useCase.updateMiniatureUserPhoto(
            UserMiniatureUpdateDto(userId, ivWidth, ivHeight, croppedWidth, croppedHeight, offsetX, offsetY)
        )
    }

    fun deletePhoto(userId: Int): Flow<Boolean> {
        return useCase.deleteUserPhoto(userId)
    }
}
