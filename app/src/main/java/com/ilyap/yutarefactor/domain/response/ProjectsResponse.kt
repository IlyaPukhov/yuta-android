package com.ilyap.yutarefactor.domain.response

import com.ilyap.yutarefactor.domain.dto.ProjectDto

data class ProjectsResponse(
    val status: String,
    val error: String?,
    val managedProjects: List<ProjectDto>?,
    val othersProjects: List<ProjectDto>?
)
