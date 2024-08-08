package com.ilyap.yuta.domain.entity

data class UserUpdateDto(
    var id: Int,
    var photo: String,
    var croppedPhoto: String,
    var lastName: String,
    var firstName: String,
    var patronymic: String?,
    var age: String,
    var biography: String?,
    var phoneNumber: String?,
    var eMail: String?,
    var vk: String?,
    var faculty: String,
    var direction: String,
    var group: String,
    var allProjectsCount: Int,
    var doneProjectsCount: Int,
    var allTasksCount: Int,
    var doneTasksCount: Int,
    var teamsCount: Int
)
