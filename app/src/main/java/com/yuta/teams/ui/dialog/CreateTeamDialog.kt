package com.yuta.teams.ui.dialog

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
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
        setupEditViews()
        initializeRecyclerViews()
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

    private fun initializeRecyclerViews() {
        dialog.findViewById<RecyclerView>(R.id.addedMembers).apply {
            layoutManager = LinearLayoutManager(fragment.requireContext())
            membersAdapter = TeamUserSearchAdapter(
                teamViewModel.addedMembers,
            ) { updateAddedTextVisibility() }
            adapter = membersAdapter
        }

        dialog.findViewById<RecyclerView>(R.id.searchUsers).apply {
            layoutManager = LinearLayoutManager(fragment.requireContext())
            searchAdapter = TeamUserSearchAdapter(
                mutableListOf(),
                membersAdapter
            ) { updateAddedTextVisibility() }
            adapter = searchAdapter
        }
    }

    private fun setupEditViews() {
        teamName.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) return@doOnTextChanged
            checkNameUnique(text.toString()) { isUnique ->
                submitButton.isEnabled = isUnique
                error.isVisible = !isUnique
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
        teamViewModel.viewModelScope.launch {
            teamViewModel.searchUsers(text, UserUtils.getUserId(fragment.requireContext()), members)
                .collect { updateList(searchAdapter, it) }
        }
    }

    private fun updateList(adapter: TeamUserSearchAdapter, users: List<UserDto>) {
        emptySearch.isVisible = users.isEmpty()
        adapter.refillList(users)
    }

    fun updateAddedTextVisibility() {
        addedText.isVisible = teamViewModel.addedMembers.isNotEmpty()
    }
}
