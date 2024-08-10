package com.yuta.data.repository

import com.yuta.data.network.ProjectsApiService
import com.yuta.domain.model.Team
import com.yuta.domain.model.Project
import com.yuta.domain.model.ProjectCreateDto
import com.yuta.domain.model.ProjectEditDto
import com.yuta.domain.model.ProjectsDto
import com.yuta.domain.repository.ProjectsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import javax.inject.Inject

class ProjectsRepositoryImpl @Inject constructor(
    private val apiService: ProjectsApiService
) : ProjectsRepository {

    override fun getAllProjects(userId: Int): Flow<ProjectsDto> = flow {
        val projectsResponse = apiService.getAllProjectsByUserId(userId)
        emit(
            ProjectsDto(
                managedProjects = projectsResponse.managedProjects,
                othersProjects = projectsResponse.othersProjects
            )
        )
    }

    override fun getProjectById(projectId: Int): Flow<Project?> = flow {
        emit(apiService.getProjectById(projectId).project)
    }

    override fun searchTeamsForProject(
        teamName: String,
        leaderId: Int,
        teamId: Int?
    ): Flow<List<Team>?> = flow {
        emit(apiService.searchTeamsForProject(teamName, leaderId, teamId).teams)
    }

    override fun createProject(createDto: ProjectCreateDto): Flow<String> = flow {
        val technicalTaskPart = createTechnicalTaskPart(createDto.technicalTask, createDto.filename)
        emit(
            apiService.createProject(
                managerId = createDto.managerId,
                projectName = createDto.name,
                description = createDto.description,
                deadline = createDto.deadline,
                teamId = createDto.teamId,
                technicalTask = technicalTaskPart
            ).status
        )
    }

    override fun editProject(editDto: ProjectEditDto): Flow<String> = flow {
        val technicalTaskPart = createTechnicalTaskPart(editDto.technicalTask, editDto.filename)
        emit(
            apiService.editProject(
                projectId = editDto.id,
                projectName = editDto.name,
                description = editDto.description,
                deadline = editDto.deadline,
                teamId = editDto.teamId,
                status = editDto.status.name,
                technicalTask = technicalTaskPart
            ).status
        )
    }

    private fun createTechnicalTaskPart(inputStream: InputStream?, filename: String?): MultipartBody.Part? {
        return inputStream?.let {
            val requestBody = it.readBytes().toRequestBody("application/pdf".toMediaType())
            MultipartBody.Part.createFormData("project_technical_task", filename, requestBody)
        }
    }

    override fun deleteProject(projectId: Int): Flow<String> = flow {
        emit(apiService.deleteProject(projectId).status)
    }
}
