package com.yuta.teams.ui.dialog

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.yuta.app.R
import com.yuta.common.util.FieldUtils.trimmedText
import com.yuta.common.util.KeyboardUtils
import com.yuta.domain.model.Team
import com.yuta.domain.model.UserDto
import com.yuta.teams.viewmodel.TeamDialogsViewModel
import kotlinx.coroutines.launch

class EditTeamDialog(
    private val fragment: Fragment,
    private val teamId: Int,
    private val onEditSuccessCallback: () -> Unit
) : CreateTeamDialog(fragment) {

    private val title: TextView by lazy { dialog.findViewById(R.id.title) }

    private val teamViewModel: TeamDialogsViewModel by fragment.viewModels()

    override fun start() {
        super.start()
        setupViews()
        getTeamDetails(teamId)

        submitButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), submitButton)
            editTeam(teamId, teamName.trimmedText(), teamViewModel.addedMembers)
        }
    }

    private fun setupViews() {
        submitButton.apply {
            text = context.getString(R.string.save_button)
            isEnabled = false
        }
        title.text = context.getString(R.string.edit_team_text)
    }

    private fun getTeamDetails(teamId: Int) {
        teamViewModel.viewModelScope.launch {
            teamViewModel.getById(teamId).collect { populateTeamDetails(it) }
        }
    }

    private fun populateTeamDetails(team: Team) {
        teamName.setText(team.name)
        membersAdapter.refillList(team.members)
        updateAddedTextVisibility()
    }

    private fun editTeam(teamId: Int, name: String, members: List<UserDto>) {
        if (name.isEmpty()) return

        teamViewModel.viewModelScope.launch {
            teamViewModel.edit(teamId, name, members).collect { result ->
                if (result) {
                    onEditSuccessCallback()
                }
            }
        }
    }

    override fun checkNameUnique(name: String, onUniqueCallback: (Boolean) -> Unit) {
        teamViewModel.viewModelScope.launch {
            teamViewModel.isUniqueName(name, teamId).collect { onUniqueCallback(it) }
        }
    }
}
