package com.ilyap.yutarefactor.domain.response

import com.ilyap.yutarefactor.domain.entity.Project

data class ProjectResponse(
    val status: String,
    val error: String?,
    val project: Project?
)
