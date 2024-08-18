package com.yuta.teams.ui.dialog

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.common.util.FieldUtils.trimmedText
import com.yuta.common.util.KeyboardUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.UserDto
import com.yuta.teams.ui.adapter.TeamUserSearchAdapter
import com.yuta.teams.viewmodel.TeamDialogsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

open class CreateTeamDialog(
    private val fragment: Fragment,
    private val onCreateSuccessCallback: () -> Unit? = {}
) : CancelableDialog(R.layout.dialog_create_team, fragment.requireActivity()) {

    protected val teamName: EditText by lazy { dialog.findViewById(R.id.team_name) }
    protected val submitButton: Button by lazy { dialog.findViewById(R.id.submit) }
    private val closeButton: Button by lazy { dialog.findViewById(R.id.close) }
    private val searchButton: Button by lazy { dialog.findViewById(R.id.btnSearch) }
    private val searchField: EditText by lazy { dialog.findViewById(R.id.find_name) }
    private val error: TextView by lazy { dialog.findViewById(R.id.error_text) }
    private val emptySearch: TextView by lazy { dialog.findViewById(R.id.empty_search_text) }
    private val addedText: TextView by lazy { dialog.findViewById(R.id.added_members) }
    private lateinit var searchAdapter: TeamUserSearchAdapter
    protected lateinit var membersAdapter: TeamUserSearchAdapter

    private val teamViewModel: TeamDialogsViewModel by fragment.viewModels()

    override fun start() {
        super.start()
        setupButtons()
        setupTextWatchers()
        setupRecyclerViews()
    }

    private fun setupButtons() {
        closeButton.setOnClickListener { dismiss() }

        searchButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), searchButton)
            searchUsers(searchField.trimmedText(), teamViewModel.addedMembers)
        }

        submitButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), teamName)
            createTeam(teamName.trimmedText(), teamViewModel.addedMembers)
        }
    }

    private fun setupRecyclerViews() {
        dialog.findViewById<RecyclerView>(R.id.addedMembers).apply {
            layoutManager = LinearLayoutManager(context)
            membersAdapter = TeamUserSearchAdapter(
                teamViewModel.addedMembers,
                this@CreateTeamDialog
            ) { updateAddedTextVisibility() }
            adapter = membersAdapter
        }

        dialog.findViewById<RecyclerView>(R.id.searchUsers).apply {
            layoutManager = LinearLayoutManager(context)
            searchAdapter = TeamUserSearchAdapter(
                mutableListOf(),
                this@CreateTeamDialog,
                membersAdapter
            ) { updateAddedTextVisibility() }
            adapter = searchAdapter
        }
    }

    private fun setupTextWatchers() {
        teamName.doOnTextChanged { text, _, _, _ ->
            checkNameUnique(text.toString()) { isUnique ->
                submitButton.isEnabled = isUnique
                messageVisibility(error, !isUnique)
            }
        }

        searchField.doOnTextChanged { text, _, _, _ ->
            searchButton.isEnabled = !text.isNullOrBlank()
        }
    }

    protected open fun checkNameUnique(name: String, onUniqueCallback: (Boolean) -> Unit) {
        teamViewModel.viewModelScope.launch {
            teamViewModel.isUniqueName(name).collect { onUniqueCallback(it) }
        }
    }

    private fun createTeam(name: String, members: List<UserDto>) {
        if (name.isEmpty()) return

        teamViewModel.viewModelScope.launch {
            val isCreated = teamViewModel.create(UserUtils.getUserId(fragment.requireContext()), name, members).first()
            handleCreateResult(isCreated)
        }
    }

    private fun handleCreateResult(isCreated: Boolean) {
        if (isCreated) {
            onCreateSuccessCallback()
            dismiss()
        }
    }

    private fun searchUsers(text: String, members: List<UserDto>) {
        if (text.isEmpty()) return

        teamViewModel.viewModelScope.launch {
            teamViewModel.searchUsers(text, UserUtils.getUserId(fragment.requireContext()), members)
                .collect { updateList(searchAdapter, it) }
        }
    }

    private fun updateList(adapter: TeamUserSearchAdapter, users: List<UserDto>) {
        messageVisibility(emptySearch, users.isEmpty())
        adapter.refillList(users)
    }

    fun updateAddedTextVisibility() = messageVisibility(addedText, teamViewModel.addedMembers.isNotEmpty())

    private fun messageVisibility(message: View, condition: Boolean) {
        message.visibility = if (condition) VISIBLE else GONE
    }
}
