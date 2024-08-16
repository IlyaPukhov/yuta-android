package com.yuta.teams.ui.dialog

import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.domain.model.Team
import com.yuta.teams.viewmodel.TeamDialogsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DeleteTeamDialog(
    fragment: Fragment,
    private val team: Team,
    private val onDeleteSuccessCallback: () -> Unit
) : CancelableDialog(R.layout.dialog_delete, fragment.requireActivity()) {

    private val submitButton: Button by lazy { dialog.findViewById(R.id.submit) }
    private val closeButton: Button by lazy { dialog.findViewById(R.id.close) }
    private val description: TextView by lazy { dialog.findViewById(R.id.desc) }

    private val teamViewModel: TeamDialogsViewModel by fragment.viewModels()

    override fun start() {
        super.start()
        setupTextView(team.name)

        closeButton.setOnClickListener { dismiss() }
        submitButton.setOnClickListener { deleteTeam(team.id) }
    }

    private fun setupTextView(name: String) {
        val text = "${context.getString(R.string.delete_team_desc)} \"$name\"?"
        description.text = text
    }

    private fun deleteTeam(id: Int) {
        teamViewModel.viewModelScope.launch {
            val isDeleted = teamViewModel.delete(id).first()
            handleDeleteResult(isDeleted)
        }
    }

    private fun handleDeleteResult(isDeleted: Boolean) {
        if (isDeleted) {
            onDeleteSuccessCallback()
            dismiss()
        }
    }
}
