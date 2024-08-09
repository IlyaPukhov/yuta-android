package com.yuta.domain.teams.repository

import com.yuta.domain.common.model.Team
import com.yuta.domain.common.model.UserDto
import com.yuta.domain.teams.model.TeamCreateDto
import com.yuta.domain.teams.model.TeamEditDto
import com.yuta.domain.teams.model.TeamsDto
import kotlinx.coroutines.flow.Flow

interface TeamsRepository {

    fun getAllTeams(userId: Int): Flow<TeamsDto>

    fun getTeamById(teamId: Int): Flow<Team?>

    fun searchUsersForTeam(username: String, leaderId: Int, members: List<UserDto>): Flow<List<UserDto>?>

    fun checkUniqueTeamName(teamName: String, teamId: Int? = null): Flow<Boolean?>

    fun createTeam(createDto: TeamCreateDto): Flow<String>

    fun editTeam(editDto: TeamEditDto): Flow<String>

    fun deleteTeam(teamId: Int): Flow<String>
}
