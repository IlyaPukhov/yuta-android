package com.yuta.data.repository

import com.yuta.data.network.TeamsApiService
import com.yuta.domain.model.Team
import com.yuta.domain.model.User
import com.yuta.domain.model.dto.TeamCreateDto
import com.yuta.domain.model.dto.TeamEditDto
import com.yuta.domain.model.dto.TeamsDto
import com.yuta.domain.repository.TeamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import javax.inject.Inject

class TeamsRepositoryImpl @Inject constructor(
    private val apiService: TeamsApiService
) : TeamsRepository {

    override fun getAllTeams(userId: Int): Flow<TeamsDto> = flow {
        val teamsResponse = apiService.getAllTeamsByUserId(userId)
        emit(
            TeamsDto(
                managedTeams = teamsResponse.managedTeams,
                othersTeams = teamsResponse.othersTeams
            )
        )
    }

    override fun getTeamById(teamId: Int): Flow<Team?> = flow {
        emit(apiService.getTeamById(teamId).team)
    }

    override fun searchUsersForTeam(
        username: String,
        leaderId: Int,
        members: List<User>
    ): Flow<List<User>?> = flow {
        emit(apiService.searchUsersForTeam(username, leaderId, getMembersIdArray(members)).users)
    }

    override fun checkUniqueTeamName(teamName: String, teamId: Int?): Flow<Boolean?> = flow {
        emit(apiService.checkUniqueTeamName(teamName, teamId).unique)
    }

    override fun createTeam(createDto: TeamCreateDto): Flow<String> = flow {
        emit(
            apiService.createTeam(
                leaderId = createDto.leaderId,
                teamName = createDto.name,
                membersIds = getMembersIdArray(createDto.members)
            ).status
        )
    }

    override fun editTeam(editDto: TeamEditDto): Flow<String> = flow {
        emit(
            apiService.editTeam(
                teamId = editDto.id,
                teamName = editDto.name,
                membersIds = getMembersIdArray(editDto.members)
            ).status
        )
    }

    private fun getMembersIdArray(members: List<User>): JSONArray {
        return members.fold(JSONArray()) { jsonArray, user ->
            jsonArray.put(user.id)
            jsonArray
        }
    }

    override fun deleteTeam(teamId: Int): Flow<String> = flow {
        emit(apiService.deleteTeam(teamId).status)
    }
}
