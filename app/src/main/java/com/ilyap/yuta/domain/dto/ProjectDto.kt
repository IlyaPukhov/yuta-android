package com.ilyap.yuta.domain.dto

import com.ilyap.yuta.domain.entity.Team
import com.ilyap.yuta.domain.entity.UserUpdateDto

data class ProjectDto(
    val id: Int,
    val photo: String?,
    val name: String,
    val technicalTask: String?,
    val status: String,
    val stringDeadline: String,
    val description: String,
    val manager: UserUpdateDto,
    val team: Team?
)
