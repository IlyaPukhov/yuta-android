package com.yuta.domain.model.dto

import com.yuta.domain.model.ProjectDto

data class ProjectsDto(
    val managedProjects: List<ProjectDto>?,
    val othersProjects: List<ProjectDto>?
)
