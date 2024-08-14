package com.yuta.teams.ui.dialog

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.yuta.app.R
import com.yuta.common.util.KeyboardUtils
import com.yuta.domain.model.Team
import com.yuta.domain.model.UserDto
import com.yuta.teams.viewmodel.TeamsViewModel
import kotlinx.coroutines.launch

class EditTeamDialog(
    private val fragment: Fragment,
    private val teamId: Int,
    private val onEditSuccess: () -> Unit
) : CreateTeamDialog(fragment) {

    private val teamsViewModel: TeamsViewModel by fragment.viewModels()

    override fun start() {
        super.start()
        setupViews()
        getTeam(teamId)

        submitButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), submitButton)
            editTeam(teamId, teamName.trimmedText(), addedMembers)
        }
    }

    private fun setupViews() {
        submitButton.apply {
            this.setText(R.string.save_button)
            this.isEnabled = false
        }
        dialog.findViewById<TextView>(R.id.create_text).text = context.getString(R.string.edit_team_text)
    }

    private fun getTeam(teamId: Int) {
        teamsViewModel.viewModelScope.launch {
            teamsViewModel.getById(teamId).collect { setupTeam(it) }
        }
    }

    private fun setupTeam(team: Team) {
        teamName.setText(team.name)
        membersAdapter.refillList(team.members)
        updateAddedTextVisibility()
    }

    private fun editTeam(teamId: Int, name: String, members: List<UserDto>) {
        if (name.isEmpty()) return

        teamsViewModel.viewModelScope.launch {
            teamsViewModel.edit(teamId, name, members).collect { result ->
                if (result) {
                    onEditSuccess()
                }
            }
        }
    }

    override fun checkNameUnique(name: String, onUniqueCallback: (Boolean) -> Unit) {
        teamsViewModel.viewModelScope.launch {
            teamsViewModel.isUniqueName(name, teamId).collect { onUniqueCallback(it) }
        }
    }
}
