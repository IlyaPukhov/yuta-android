package com.yuta.domain.model

import java.io.InputStream

data class ProjectCreateDto(
    val managerId: Int,
    val name: String,
    val description: String,
    val deadline: String,
    val teamId: Int?,

    val filename: String?,
    val technicalTask: InputStream?
)
