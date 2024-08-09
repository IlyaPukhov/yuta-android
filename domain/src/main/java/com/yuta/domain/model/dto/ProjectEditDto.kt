package com.yuta.domain.model.dto

import java.io.InputStream

data class ProjectEditDto(
    val id: Int,
    val name: String,
    val description: String,
    val deadline: String,
    val teamId: Int? = null,
    val status: String,

    val filename: String? = null,
    val technicalTask: InputStream? = null
)
