package com.yuta.search.viewmodel

import androidx.lifecycle.ViewModel
import com.yuta.domain.model.UserDto
import com.yuta.domain.usecase.SearchUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val useCase: SearchUseCase
) : ViewModel() {

    var usersList: MutableList<UserDto> = mutableListOf()

    fun search(text: String): Flow<List<UserDto>> {
        return useCase.searchUsers(text)
    }
}
