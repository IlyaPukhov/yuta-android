package com.yuta.domain.usecase

import com.yuta.domain.model.Project
import com.yuta.domain.model.ProjectCreateDto
import com.yuta.domain.model.ProjectEditDto
import com.yuta.domain.model.ProjectsDto
import com.yuta.domain.model.Team
import kotlinx.coroutines.flow.Flow

interface ProjectsUseCase {

    fun getAllProjects(userId: Int): Flow<ProjectsDto>

    fun getProjectById(projectId: Int): Flow<Project>

    fun searchTeamsForProject(teamName: String, leaderId: Int, teamId: Int? = null): Flow<List<Team>>

    fun createProject(projectCreateDto: ProjectCreateDto): Flow<Void>

    fun editProject(projectEditDto: ProjectEditDto): Flow<Void>

    fun deleteProject(projectId: Int): Flow<Void>
}
