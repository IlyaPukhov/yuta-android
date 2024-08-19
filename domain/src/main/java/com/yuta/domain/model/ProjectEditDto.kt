package com.yuta.domain.model

import java.io.InputStream

data class ProjectEditDto(
    val id: Int,
    val name: String,
    val description: String,
    val deadline: String,
    val teamId: Int?,
    val status: ProjectStatus,

    val filename: String?,
    val technicalTask: InputStream?
)
