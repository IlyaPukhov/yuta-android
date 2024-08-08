package com.ilyap.yuta.domain.response

import com.ilyap.yuta.domain.entity.Project

data class ProjectResponse(
    val status: String,
    val error: String?,
    val project: Project?
)
