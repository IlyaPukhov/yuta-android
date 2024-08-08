package com.ilyap.yuta.data.model

import com.ilyap.yuta.domain.model.entity.Project

data class ProjectResponse(
    val status: String,
    val error: String?,
    val project: Project?
)
