package com.yuta.domain.usecase.impl

import com.yuta.domain.model.Project
import com.yuta.domain.model.ProjectCreateDto
import com.yuta.domain.model.ProjectEditDto
import com.yuta.domain.model.ProjectsDto
import com.yuta.domain.model.Team
import com.yuta.domain.repository.ProjectsRepository
import com.yuta.domain.usecase.ProjectsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectsUseCaseImpl @Inject constructor(
    private val repository: ProjectsRepository
) : ProjectsUseCase {

    override fun getAllProjects(userId: Int): Flow<ProjectsDto> {
        TODO("Not yet implemented")
    }

    override fun getProjectById(projectId: Int): Flow<Project> {
        TODO("Not yet implemented")
    }

    override fun searchTeamsForProject(
        teamName: String,
        leaderId: Int,
        teamId: Int?
    ): Flow<List<Team>> {
        TODO("Not yet implemented")
    }

    override fun createProject(projectCreateDto: ProjectCreateDto): Flow<Void> {
        TODO("Not yet implemented")
    }

    override fun editProject(projectEditDto: ProjectEditDto): Flow<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projectId: Int): Flow<Void> {
        TODO("Not yet implemented")
    }
}
