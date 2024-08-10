package com.yuta.domain.usecase

import com.yuta.domain.model.Team
import com.yuta.domain.model.TeamCreateDto
import com.yuta.domain.model.TeamEditDto
import com.yuta.domain.model.TeamsDto
import com.yuta.domain.model.UserDto
import kotlinx.coroutines.flow.Flow

interface TeamsUseCase {

    fun getAllTeams(userId: Int): Flow<TeamsDto>

    fun getTeamById(teamId: Int): Flow<Team>

    fun searchUsersForTeam(username: String, leaderId: Int, members: List<UserDto>): Flow<List<UserDto>>

    fun checkUniqueTeamName(teamName: String, teamId: Int? = null): Flow<Boolean>

    fun createTeam(teamCreateDto: TeamCreateDto): Flow<Boolean>

    fun editTeam(teamEditDto: TeamEditDto): Flow<Boolean>

    fun deleteTeam(teamId: Int): Flow<Boolean>
}
