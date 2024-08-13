package com.yuta.teams.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.usecase.TeamsUseCase
import javax.inject.Inject

class TeamsViewModel @Inject constructor(
    private val useCase: TeamsUseCase
) : ViewModel() {

}
