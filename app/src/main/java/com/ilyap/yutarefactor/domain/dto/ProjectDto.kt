package com.ilyap.yutarefactor.domain.dto

import com.ilyap.yutarefactor.domain.entity.Team
import com.ilyap.yutarefactor.domain.entity.UserUpdateDto

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
