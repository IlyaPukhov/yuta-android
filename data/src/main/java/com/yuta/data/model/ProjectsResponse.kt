package com.yuta.data.model

import com.yuta.domain.model.ProjectDto

data class ProjectsResponse(
    val status: String,
    val error: String?,
    val managedProjects: List<ProjectDto>?,
    val othersProjects: List<ProjectDto>?
)
