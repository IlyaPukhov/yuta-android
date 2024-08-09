package com.yuta.domain.repository

import com.yuta.domain.model.Team
import com.yuta.domain.model.User
import com.yuta.domain.model.dto.TeamCreateDto
import com.yuta.domain.model.dto.TeamEditDto
import com.yuta.domain.model.dto.TeamsDto
import kotlinx.coroutines.flow.Flow

interface TeamsRepository {

    fun getAllTeams(userId: Int): Flow<TeamsDto>

    fun getTeamById(teamId: Int): Flow<Team?>

    fun searchUsersForTeam(username: String, leaderId: Int, members: List<User>): Flow<List<User>?>

    fun checkUniqueTeamName(teamName: String, teamId: Int? = null): Flow<Boolean?>

    fun createTeam(createDto: TeamCreateDto): Flow<String>

    fun editTeam(editDto: TeamEditDto): Flow<String>

    fun deleteTeam(teamId: Int): Flow<String>
}
