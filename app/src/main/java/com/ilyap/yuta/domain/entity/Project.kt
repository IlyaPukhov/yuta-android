package com.ilyap.yuta.domain.entity

data class Project(
    val id: Int,
    val name: String,
    val technicalTask: String?,
    val technicalTaskName: String?,
    val deadline: String,
    val status: String,
    val description: String,
    val team: Team?
)
