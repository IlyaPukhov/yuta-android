package com.ilyap.yuta.domain.response

import com.ilyap.yuta.domain.dto.ProjectDto

data class ProjectsResponse(
    val status: String,
    val error: String?,
    val managedProjects: List<ProjectDto>?,
    val othersProjects: List<ProjectDto>?
)
