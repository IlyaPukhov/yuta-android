package com.yuta.projects.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.model.ProjectsDto
import com.yuta.domain.model.Team
import com.yuta.domain.model.TeamMember
import com.yuta.domain.model.TeamsDto
import com.yuta.domain.usecase.ProjectsUseCase
import com.yuta.domain.usecase.TeamsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectsViewModel @Inject constructor(
    private val useCase: ProjectsUseCase
) : ViewModel() {

    fun getAll(userId: Int): Flow<ProjectsDto> {
        return useCase.getAllProjects(userId)
    }
}
