package com.yuta.domain.model

data class UserDto(
    var id: Int,
    var croppedPhoto: String,
    var lastName: String,
    var firstName: String,
    var patronymic: String?,
)
