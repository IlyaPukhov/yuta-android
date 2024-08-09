package com.yuta.domain.model.dto

import java.io.InputStream

data class ProjectCreateDto(
    val managerId: Int,
    val name: String,
    val description: String,
    val deadline: String,
    val teamId: Int? = null,

    val filename: String? = null,
    val technicalTask: InputStream? = null
)
