package com.yuta.teams.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.model.Team
import com.yuta.domain.model.TeamMember
import com.yuta.domain.model.TeamsDto
import com.yuta.domain.usecase.TeamsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TeamsViewModel @Inject constructor(
    private val useCase: TeamsUseCase
) : ViewModel() {

    var lastPickedButtonId: Int? = null
    lateinit var managedTeamsMembers: List<List<TeamMember>>
    lateinit var othersTeamsMembers: List<List<TeamMember>>

    fun getAll(userId: Int): Flow<TeamsDto> {
        return useCase.getAllTeams(userId)
    }

    fun initializeTeams(dto: TeamsDto) {
        managedTeamsMembers = getTeamMembers(dto.managedTeams!!)
        othersTeamsMembers = getTeamMembers(dto.othersTeams!!)
    }

    private fun getTeamMembers(teams: List<Team>): List<List<TeamMember>> {
        return teams.map { team ->
            val membersList = ArrayList<TeamMember>()
            membersList.add(TeamMember(team, team.leader))
            membersList.addAll(team.members.map { member -> TeamMember(team, member) })
            membersList
        }
    }
}
