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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditTeamDialog(
    private val fragment: Fragment,
    private val team: Team,
    private val onEditSuccessCallback: () -> Unit
) : CreateTeamDialog(fragment) {

    private val title: TextView by lazy { dialog.findViewById(R.id.title) }

    private val teamViewModel: TeamDialogsViewModel by fragment.viewModels()

    override fun start() {
        super.start()
        setupViews()
        populateTeamDetails(team)

        submitButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), submitButton)
            editTeam(team.id, teamName.trimmedText(), teamViewModel.addedMembers)
        }
    }

    private fun setupViews() {
        submitButton.apply {
            setText(R.string.save_button)
            isEnabled = false
        }
        title.setText(R.string.edit_team_text)
    }

    private fun populateTeamDetails(team: Team) {
        teamName.setText(team.name)
        membersAdapter.refillList(team.members)
        updateAddedTextVisibility()
    }

    private fun editTeam(teamId: Int, name: String, members: List<UserDto>) {
        teamViewModel.viewModelScope.launch {
            val isEdited = teamViewModel.edit(teamId, name, members).first()
            handleEditResult(isEdited)
        }
    }

    private fun handleEditResult(isEdited: Boolean) {
        if (isEdited) {
            onEditSuccessCallback()
            dismiss()
        }
    }

    override fun checkNameUnique(name: String, onUniqueCallback: (Boolean) -> Unit) {
        teamViewModel.viewModelScope.launch {
            teamViewModel.isUniqueName(name, team.id).collect { onUniqueCallback(it) }
        }
    }
}
