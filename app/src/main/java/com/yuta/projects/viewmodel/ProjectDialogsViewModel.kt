package com.yuta.projects.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.model.Project
import com.yuta.domain.model.ProjectCreateDto
import com.yuta.domain.model.ProjectEditDto
import com.yuta.domain.model.ProjectStatus
import com.yuta.domain.model.Team
import com.yuta.domain.usecase.ProjectsUseCase
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import javax.inject.Inject

class ProjectDialogsViewModel @Inject constructor(
    private val useCase: ProjectsUseCase
) : ViewModel() {

    fun getById(id: Int): Flow<Project> {
        return useCase.getProjectById(id)
    }

    fun searchTeams(text: String, userId: Int, teamId: Int? = null): Flow<List<Team>> {
        return useCase.searchTeamsForProject(text, userId, teamId)
    }

    fun create(
        userId: Int, name: String, description: String, deadline: String, teamId: Int? = null,
        filename: String? = null, technicalTask: InputStream? = null
    ): Flow<Boolean> {
        return useCase.createProject(
            ProjectCreateDto(
                managerId = userId,
                name,
                description,
                deadline,
                teamId,
                filename,
                technicalTask
            )
        )
    }

    fun edit(
        id: Int, name: String, description: String, deadline: String, teamId: Int? = null, status: String,
        filename: String? = null, technicalTask: InputStream? = null
    ): Flow<Boolean> {
        return useCase.editProject(
            ProjectEditDto(
                id,
                name,
                description,
                deadline,
                teamId,
                ProjectStatus.fromText(status),
                filename,
                technicalTask
            )
        )
    }

    fun delete(id: Int): Flow<Boolean> {
        return useCase.deleteProject(id)
    }
}
