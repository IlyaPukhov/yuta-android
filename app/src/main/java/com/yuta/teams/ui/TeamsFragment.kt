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

    private val teamsViewModel: TeamsViewModel by viewModels()

    private lateinit var managedTeamsButton: ToggleButton
    private lateinit var memberTeamsButton: ToggleButton
    private lateinit var emptyText: TextView
    private lateinit var progressLayout: View
    private lateinit var teamsAdapter: TeamsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_teams, container, false).also {
            setupRecyclerView(it)
            teamsSwitchInitialize(it)
            setupViews(it)
        }
    }

    override fun onStart() {
        super.onStart()
        updateLists()
    }

    fun updateLists() {
        updateLists(view!!.findViewById<Button>(teamsViewModel.lastPickedButtonId!!))
    }

    private fun updateLists(button: Button) {
        progressLayout.visibility = VISIBLE
        updateTeams {
            button.performClick()
            progressLayout.visibility = GONE
        }
    }

    private fun onToggleButtonClick(view: View) {
        val button = view as ToggleButton
        val otherButton: ToggleButton

        if (button.id == managedTeamsButton.id) {
            otherButton = memberTeamsButton
            fillTeams(teamsViewModel.managedTeamsMembers)
        } else {
            otherButton = managedTeamsButton
            fillTeams(teamsViewModel.othersTeamsMembers)
        }

        button.setTextAppearance(R.style.active_toggle)
        button.isChecked = true
        otherButton.setTextAppearance(R.style.default_toggle)
        otherButton.isChecked = false
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

    private fun setupViews(view: View) {
        emptyText = view.findViewById(R.id.empty_text)
        progressLayout = view.findViewById(R.id.progressLayout)

        view.findViewById<Button>(R.id.create_team).setOnClickListener { openCreateTeamDialog() }
        view.findViewById<Button>(R.id.log_out).setOnClickListener { openLogoutDialog() }
    }

    private fun setupRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(requireContext())
            teamsAdapter = TeamsAdapter(requireActivity(), mutableListOf(), this@TeamsFragment)
            adapter = teamsAdapter
        }
    }

    private fun teamsSwitchInitialize(view: View) {
        managedTeamsButton = view.findViewById(R.id.manager_button)
        memberTeamsButton = view.findViewById(R.id.member_button)
        managedTeamsButton.setOnClickListener(this::onToggleButtonClick)
        memberTeamsButton.setOnClickListener(this::onToggleButtonClick)

        teamsViewModel.lastPickedButtonId = teamsViewModel.lastPickedButtonId ?: memberTeamsButton.id
    }

    private fun openCreateTeamDialog() = CreateTeamDialog(fragment = this) { updateLists() }.start()

    private fun openLogoutDialog() = LogoutDialog(fragment = this).start()
}
