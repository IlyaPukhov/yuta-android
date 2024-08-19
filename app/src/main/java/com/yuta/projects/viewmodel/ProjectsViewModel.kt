package com.yuta.projects.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.model.ProjectsDto
import com.yuta.domain.usecase.ProjectsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectsViewModel @Inject constructor(
    private val useCase: ProjectsUseCase
) : ViewModel() {

    fun getAll(userId: Int): Flow<ProjectsDto> {
        return useCase.getAllProjects(userId)
    }
}
