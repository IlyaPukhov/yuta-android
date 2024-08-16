package com.yuta.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.usecase.ProfileUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val useCase: ProfileUseCase
) : ViewModel() {

    // TODO()
}
