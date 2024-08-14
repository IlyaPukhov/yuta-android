package com.yuta.teams.ui.dialog

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.common.util.KeyboardUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.UserDto
import com.yuta.teams.ui.adapter.TeamUserSearchAdapter
import com.yuta.teams.viewmodel.TeamsViewModel
import kotlinx.coroutines.launch

open class CreateTeamDialog(
    private val fragment: Fragment,
    private val onCreateSuccess: () -> Unit? = {}
) : CancelableDialog(R.layout.dialog_create_team, fragment.requireActivity()) {

    private val teamsViewModel: TeamsViewModel by fragment.viewModels()
    protected val addedMembers = mutableListOf<UserDto>()
    private val searchUserDtos = mutableListOf<UserDto>()

    protected lateinit var teamName: EditText
    protected lateinit var submitButton: Button
    private lateinit var searchButton: Button
    private lateinit var searchField: EditText
    private lateinit var error: TextView
    private lateinit var emptySearch: TextView
    private lateinit var addedText: TextView
    private lateinit var searchAdapter: TeamUserSearchAdapter
    protected lateinit var membersAdapter: TeamUserSearchAdapter

    override fun start() {
        super.start()
        setupViews()
        setupRecyclerViews()
    }

    private fun setupViews() {
        submitButton = dialog.findViewById(R.id.submit)
        searchButton = dialog.findViewById(R.id.btnSearch)
        teamName = dialog.findViewById(R.id.team_name)
        searchField = dialog.findViewById(R.id.find_name)
        error = dialog.findViewById(R.id.error_text)
        emptySearch = dialog.findViewById(R.id.empty_search_text)
        addedText = dialog.findViewById(R.id.added_members)

        dialog.findViewById<View>(R.id.close).setOnClickListener { dismiss() }
        searchButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), searchButton)
            searchUsers(searchField.trimmedText(), addedMembers)
        }
        submitButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), teamName)
            createTeam(teamName.trimmedText(), addedMembers)
        }

        setupTextWatchers()
    }

    private fun setupRecyclerViews() {
        dialog.findViewById<RecyclerView>(R.id.addedMembers).apply {
            layoutManager = LinearLayoutManager(context)
            membersAdapter = TeamUserSearchAdapter(this@CreateTeamDialog, addedMembers, null)
            adapter = membersAdapter
        }

        dialog.findViewById<RecyclerView>(R.id.searchUsers).apply {
            layoutManager = LinearLayoutManager(context)
            searchAdapter = TeamUserSearchAdapter(this@CreateTeamDialog, searchUserDtos, membersAdapter)
            adapter = searchAdapter
        }
    }

    private fun setupTextWatchers() {
        teamName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkNameUnique(s.toString()) { isUnique ->
                    submitButton.isEnabled = isUnique
                    messageVisibility(error, !isUnique)
                }
            }
        })

        searchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchButton.isEnabled = !s.isNullOrBlank()
            }
        })
    }

    protected open fun checkNameUnique(name: String, onUniqueCallback: (Boolean) -> Unit) {
        teamsViewModel.viewModelScope.launch {
            teamsViewModel.isUniqueName(name).collect { onUniqueCallback(it) }
        }
    }

    private fun createTeam(name: String, members: List<UserDto>) {
        if (name.isEmpty()) return

        teamsViewModel.viewModelScope.launch {
            teamsViewModel.create(UserUtils.getUserId(fragment.requireContext()), name, members)
                .collect { result -> if (result) onCreateSuccess() }
        }
    }

    private fun searchUsers(text: String, members: List<UserDto>) {
        if (text.isEmpty()) return

        teamsViewModel.viewModelScope.launch {
            teamsViewModel.searchUsers(text, members, fragment.requireContext())
                .collect { updateList(searchAdapter, it) }
        }
    }

    private fun updateList(adapter: TeamUserSearchAdapter, users: List<UserDto>) {
        messageVisibility(emptySearch, users.isEmpty())
        adapter.refillList(users)
    }

    fun updateAddedTextVisibility() = messageVisibility(addedText, addedMembers.isNotEmpty())

    private fun messageVisibility(message: View, condition: Boolean) {
        message.visibility = if (condition) VISIBLE else GONE
    }

    protected fun EditText.trimmedText(): String = this.text.toString().trim()
}
