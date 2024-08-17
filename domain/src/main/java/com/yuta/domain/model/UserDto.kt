package com.yuta.domain.model

data class UserDto(
    var id: Int,
    var croppedPhoto: String,
    var lastName: String,
    var firstName: String,
    var patronymic: String?,
) {
    companion object {
        fun fromUser(id: Int, user: User) = UserDto(
            id = id,
            croppedPhoto = user.croppedPhoto,
            lastName = user.lastName,
            firstName = user.firstName,
            patronymic = user.patronymic
        )
    }
}
