package com.ilyap.yuta.domain.model

data class ProjectDto(
    val id: Int,
    val photo: String?,
    val name: String,
    val technicalTask: String?,
    val status: String,
    val stringDeadline: String,
    val description: String,
    val manager: User,
    val team: Team?
)
