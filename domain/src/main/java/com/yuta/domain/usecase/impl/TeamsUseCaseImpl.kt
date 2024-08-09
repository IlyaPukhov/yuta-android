package com.yuta.domain.usecase.impl

import com.yuta.domain.model.Team
import com.yuta.domain.model.TeamCreateDto
import com.yuta.domain.model.TeamEditDto
import com.yuta.domain.model.TeamsDto
import com.yuta.domain.model.UserDto
import com.yuta.domain.repository.TeamsRepository
import com.yuta.domain.usecase.TeamsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TeamsUseCaseImpl @Inject constructor(
    private val repository: TeamsRepository
) : TeamsUseCase {

    override fun getAllTeams(userId: Int): Flow<TeamsDto> {
        TODO("Not yet implemented")
    }

    override fun getTeamById(teamId: Int): Flow<Team> {
        TODO("Not yet implemented")
    }

    override fun searchUsersForTeam(
        username: String,
        leaderId: Int,
        members: List<UserDto>
    ): Flow<List<UserDto>> {
        TODO("Not yet implemented")
    }

    override fun checkUniqueTeamName(
        teamName: String,
        teamId: Int?
    ): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun createTeam(teamCreateDto: TeamCreateDto): Flow<Void> {
        TODO("Not yet implemented")
    }

    override fun editTeam(teamEditDto: TeamEditDto): Flow<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteTeam(teamId: Int): Flow<Void> {
        TODO("Not yet implemented")
    }
}
