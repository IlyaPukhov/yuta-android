package com.yuta.domain.repository

import com.yuta.domain.model.Team
import com.yuta.domain.model.UserDto
import com.yuta.domain.model.TeamCreateDto
import com.yuta.domain.model.TeamEditDto
import com.yuta.domain.model.TeamsDto
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
