package com.yuta.domain.projects.repository

import com.yuta.domain.projects.model.Project
import com.yuta.domain.common.model.Team
import com.yuta.domain.projects.model.ProjectCreateDto
import com.yuta.domain.projects.model.ProjectEditDto
import com.yuta.domain.projects.model.ProjectsDto
import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {

    fun getAllProjects(userId: Int): Flow<ProjectsDto>

    fun getProjectById(projectId: Int): Flow<Project?>

    fun searchTeamsForProject(teamName: String, leaderId: Int, teamId: Int? = null): Flow<List<Team>?>

    fun createProject(createDto: ProjectCreateDto): Flow<String>

    fun editProject(editDto: ProjectEditDto): Flow<String>

    fun deleteProject(projectId: Int): Flow<String>
}
