package com.yuta.domain.projects.model

data class ProjectsDto(
    val managedProjects: List<ProjectDto>?,
    val othersProjects: List<ProjectDto>?
)
