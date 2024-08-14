package com.yuta.teams.ui.dialog

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.domain.model.Team
import com.yuta.teams.viewmodel.TeamsViewModel
import kotlinx.coroutines.launch

class DeleteTeamDialog(
    fragment: Fragment,
    private val team: Team,
    private val onDeleteSuccess: () -> Unit
) : CancelableDialog(R.layout.dialog_delete, fragment.requireActivity()) {

    private val teamsViewModel: TeamsViewModel by fragment.viewModels()

    override fun start() {
        super.start()
        setupTextView(team.name)

        dialog.findViewById<TextView>(R.id.close)?.setOnClickListener { dismiss() }
        dialog.findViewById<TextView>(R.id.submit)?.setOnClickListener {
            deleteTeam(team.id)
            dismiss()
        }
    }

    private fun setupTextView(name: String) {
        val text = "${context.getString(R.string.delete_team_desc)} \"$name\"?"
        dialog.findViewById<TextView>(R.id.name_desc)?.text = text
    }

    private fun deleteTeam(id: Int) {
        teamsViewModel.viewModelScope.launch {
            teamsViewModel.delete(id).collect { result ->
                if (result) {
                    onDeleteSuccess.invoke()
                }
            }
        }
    }
}
