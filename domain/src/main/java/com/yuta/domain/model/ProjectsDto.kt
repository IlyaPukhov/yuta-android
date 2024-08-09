package com.yuta.domain.model

data class ProjectsDto(
    val managedProjects: List<ProjectDto>?,
    val othersProjects: List<ProjectDto>?
)
