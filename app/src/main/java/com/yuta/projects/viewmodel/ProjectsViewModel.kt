package com.yuta.projects.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.model.ProjectDto
import com.yuta.domain.model.ProjectsDto
import com.yuta.domain.model.TeamMember
import com.yuta.domain.usecase.ProjectsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectsViewModel @Inject constructor(
    private val useCase: ProjectsUseCase
) : ViewModel() {

    var lastPickedButtonId: Int? = null
    lateinit var managedProjectsMembers: List<ProjectDto>
    lateinit var othersProjectsMembers: List<ProjectDto>

    fun getAll(userId: Int): Flow<ProjectsDto> {
        return useCase.getAllProjects(userId)
    }
}
