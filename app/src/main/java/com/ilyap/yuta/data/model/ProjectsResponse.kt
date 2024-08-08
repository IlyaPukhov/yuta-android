package com.ilyap.yuta.data.model

import com.ilyap.yuta.domain.model.entity.ProjectDto

data class ProjectsResponse(
    val status: String,
    val error: String?,
    val managedProjects: List<ProjectDto>?,
    val othersProjects: List<ProjectDto>?
)
