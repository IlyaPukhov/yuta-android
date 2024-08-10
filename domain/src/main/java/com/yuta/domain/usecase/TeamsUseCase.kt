package com.yuta.domain.usecase

import com.yuta.domain.exception.NotFoundException
import com.yuta.domain.model.Team
import com.yuta.domain.model.TeamCreateDto
import com.yuta.domain.model.TeamEditDto
import com.yuta.domain.model.TeamsDto
import com.yuta.domain.model.UserDto
import com.yuta.domain.repository.TeamsRepository
import com.yuta.domain.util.NetworkUtils.statusToBoolean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TeamsUseCase @Inject constructor(
    private val repository: TeamsRepository
) {

    fun getAllTeams(userId: Int): Flow<TeamsDto> {
        return repository.getAllTeams(userId)
            .map {
                if (it.managedTeams == null && it.othersTeams == null) {
                    throw NotFoundException("User's teams with id $userId not found!")
                }
                it
            }
    }

    fun getTeamById(teamId: Int): Flow<Team> {
        return repository.getTeamById(teamId)
            .map { it ?: throw NotFoundException("Team with id $teamId not found!") }
    }

    fun searchUsersForTeam(
        username: String,
        leaderId: Int,
        members: List<UserDto>
    ): Flow<List<UserDto>> {
        return repository.searchUsersForTeam(username, leaderId, members)
            .map { it ?: emptyList() }
    }

    fun checkUniqueTeamName(
        teamName: String,
        teamId: Int? = null
    ): Flow<Boolean> {
        return repository.checkUniqueTeamName(teamName, teamId)
            .map { it ?: throw NotFoundException("Teams with id $teamId not found!") }
    }

    fun createTeam(teamCreateDto: TeamCreateDto): Flow<Boolean> {
        return repository.createTeam(teamCreateDto)
            .map { statusToBoolean(it) }
    }

    fun editTeam(teamEditDto: TeamEditDto): Flow<Boolean> {
        return repository.editTeam(teamEditDto)
            .map { statusToBoolean(it) }
    }

    fun deleteTeam(teamId: Int): Flow<Boolean> {
        return repository.deleteTeam(teamId)
            .map { statusToBoolean(it) }
    }
}
