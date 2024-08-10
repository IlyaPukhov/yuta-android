package com.yuta.domain.usecase

import com.yuta.domain.exception.NotFoundException
import com.yuta.domain.model.Project
import com.yuta.domain.model.ProjectCreateDto
import com.yuta.domain.model.ProjectEditDto
import com.yuta.domain.model.ProjectsDto
import com.yuta.domain.model.Team
import com.yuta.domain.repository.ProjectsRepository
import com.yuta.domain.util.NetworkUtils.statusToBoolean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProjectsUseCase @Inject constructor(
    private val repository: ProjectsRepository
) {

    fun getAllProjects(userId: Int): Flow<ProjectsDto> {
        return repository.getAllProjects(userId)
            .map {
                if (it.managedProjects == null && it.othersProjects == null) {
                    throw NotFoundException("User's projects with id $userId not found!")
                }
                it
            }
    }

    fun getProjectById(projectId: Int): Flow<Project> {
        return repository.getProjectById(projectId)
            .map { it ?: throw NotFoundException("Project with id $projectId not found!") }
    }

    fun searchTeamsForProject(
        teamName: String,
        leaderId: Int,
        teamId: Int? = null
    ): Flow<List<Team>> {
        return repository.searchTeamsForProject(teamName, leaderId, teamId)
            .map { it ?: emptyList() }
    }

    fun createProject(projectCreateDto: ProjectCreateDto): Flow<Boolean> {
        return repository.createProject(projectCreateDto)
            .map { statusToBoolean(it) }
    }

    fun editProject(projectEditDto: ProjectEditDto): Flow<Boolean> {
        return repository.editProject(projectEditDto)
            .map { statusToBoolean(it) }
    }

    fun deleteProject(projectId: Int): Flow<Boolean> {
        return repository.deleteProject(projectId)
            .map { statusToBoolean(it) }
    }
}
