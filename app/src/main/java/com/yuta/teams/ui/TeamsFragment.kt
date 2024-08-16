package com.yuta.teams.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.app.R
import com.yuta.authorization.ui.LogoutDialog
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.TeamMember
import com.yuta.teams.ui.adapter.TeamsAdapter
import com.yuta.teams.ui.dialog.CreateTeamDialog
import com.yuta.teams.viewmodel.TeamsViewModel
import kotlinx.coroutines.launch

class TeamsFragment : Fragment() {

    private val createTeamButton: Button by lazy { findViewById(R.id.create_team) }
    private val logoutButton: Button by lazy { findViewById(R.id.logout) }
    private val managedTeamsButton: ToggleButton by lazy { findViewById(R.id.manager_button) }
    private val memberTeamsButton: ToggleButton by lazy { findViewById(R.id.member_button) }
    private val emptyText: TextView by lazy { findViewById(R.id.empty_text) }
    private val progressLayout: View by lazy { findViewById(R.id.progressLayout) }
    private val teamsAdapter: TeamsAdapter by lazy { TeamsAdapter(requireActivity(), mutableListOf(), this) }

    private val teamsViewModel: TeamsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_teams, container, false).also {
            setupRecyclerView(it)
            setupToggleButtons()
            setupViews()
        }
    }

    override fun onStart() {
        super.onStart()
        updateLists()
    }

    private fun updateLists() {
        teamsViewModel.lastPickedButtonId?.let {
            updateLists(findViewById<Button>(it))
        }
    }

    private fun updateLists(button: Button) {
        showProgress(true)
        updateTeams {
            button.performClick()
            showProgress(false)
        }
    }

    private fun onToggleButtonClick(view: View) {
        val button = view as ToggleButton
        val otherButton = if (button.id == managedTeamsButton.id) memberTeamsButton else managedTeamsButton

        if (button.id == managedTeamsButton.id) {
            fillTeams(teamsViewModel.managedTeamsMembers)
        } else {
            fillTeams(teamsViewModel.othersTeamsMembers)
        }

        setActiveButton(button)
        setInactiveButton(otherButton)
        teamsViewModel.lastPickedButtonId = button.id
    }

    private fun fillTeams(teamMembers: List<List<TeamMember>>) {
        emptyText.visibility = if (teamMembers.isEmpty()) VISIBLE else GONE
        teamsAdapter.refillList(teamMembers)
    }

    private fun updateTeams(onUpdateCallback: () -> Unit) {
        teamsViewModel.viewModelScope.launch {
            teamsViewModel.getAll(UserUtils.getUserId(requireContext())).collect {
                teamsViewModel.initializeTeams(it)
                onUpdateCallback()
            }
        }
    }

    private fun setupViews() {
        createTeamButton.setOnClickListener { openCreateTeamDialog() }
        logoutButton.setOnClickListener { openLogoutDialog() }
    }

    private fun setupRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = teamsAdapter
        }
    }

    private fun setupToggleButtons() {
        managedTeamsButton.setOnClickListener(this::onToggleButtonClick)
        memberTeamsButton.setOnClickListener(this::onToggleButtonClick)

        teamsViewModel.lastPickedButtonId = teamsViewModel.lastPickedButtonId ?: memberTeamsButton.id
    }

    private fun openCreateTeamDialog() = CreateTeamDialog(fragment = this) { updateLists() }.start()

    private fun openLogoutDialog() = LogoutDialog(fragment = this).start()

    private fun setActiveButton(button: ToggleButton) {
        button.setTextAppearance(R.style.active_toggle)
        button.isChecked = true
    }

    private fun setInactiveButton(button: ToggleButton) {
        button.setTextAppearance(R.style.default_toggle)
        button.isChecked = false
    }

    private fun showProgress(show: Boolean) {
        progressLayout.visibility = if (show) VISIBLE else GONE
    }

    inline fun <reified T : View> Fragment.findViewById(id: Int): T {
        return requireView().findViewById(id)
    }
}
