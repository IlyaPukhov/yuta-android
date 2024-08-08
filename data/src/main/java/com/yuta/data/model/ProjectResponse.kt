package com.yuta.data.model

import com.yuta.domain.model.Project

data class ProjectResponse(
    val status: String,
    val error: String?,
    val project: Project?
)
