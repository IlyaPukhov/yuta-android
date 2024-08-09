package com.yuta.domain.teams.model

import com.yuta.domain.common.model.Team
import com.yuta.domain.common.model.UserDto

data class TeamMember(
    val team: Team,
    val member: UserDto
)
