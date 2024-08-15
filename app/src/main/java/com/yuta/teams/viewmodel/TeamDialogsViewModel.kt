package com.yuta.teams.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.yuta.common.util.UserUtils.getUserId
import com.yuta.domain.model.Team
import com.yuta.domain.model.TeamCreateDto
import com.yuta.domain.model.TeamEditDto
import com.yuta.domain.model.UserDto
import com.yuta.domain.usecase.TeamsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TeamDialogsViewModel @Inject constructor(
    private val useCase: TeamsUseCase
) : ViewModel() {

    val addedMembers = mutableListOf<UserDto>()

    fun getById(id: Int): Flow<Team> {
        return useCase.getTeamById(id)
    }

    fun searchUsers(text: String, members: List<UserDto>, context: Context): Flow<List<UserDto>> {
        return useCase.searchUsersForTeam(text, getUserId(context), members)
    }

    fun isUniqueName(name: String, teamId: Int? = null): Flow<Boolean> {
        return useCase.checkUniqueTeamName(name, teamId)
    }

    fun create(leaderId: Int, name: String, members: List<UserDto>): Flow<Boolean> {
        return useCase.createTeam(TeamCreateDto(leaderId, name, members))
    }

    fun edit(id: Int, name: String, members: List<UserDto>): Flow<Boolean> {
        return useCase.editTeam(TeamEditDto(id, name, members))
    }

    fun delete(id: Int): Flow<Boolean> {
        return useCase.deleteTeam(id)
    }
}
