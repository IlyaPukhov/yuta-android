package com.yuta.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.app.R
import com.yuta.authorization.ui.LogoutDialog
import com.yuta.domain.model.UserDto
import com.yuta.search.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private val emptyText: View by lazy { requireView().findViewById(R.id.empty_text) }
    private val progressLayout: View by lazy { requireView().findViewById(R.id.progressLayout) }
    private val searchField: EditText by lazy { requireView().findViewById(R.id.search_user) }
    private val recyclerView: RecyclerView by lazy { requireView().findViewById(R.id.search_list) }
    private val logoutButton: Button by lazy { requireView().findViewById(R.id.logout) }

    private lateinit var searchAdapter: UserSearchAdapter

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        initializeRecyclerView()
        setupEditView()

        logoutButton.setOnClickListener { openLogoutDialog() }
        return view
    }

    override fun onResume() {
        super.onResume()
        updateList(searchViewModel.usersList)
    }

    private fun search(searchName: String, updateQueryCallback: () -> Unit) {
        showProgress(true)
        searchViewModel.viewModelScope.launch {
            searchViewModel.search(searchName).collect {
                searchViewModel.usersList = it.toMutableList()
                updateQueryCallback()
            }
        }
    }

    private fun updateList(users: List<UserDto>) {
        searchAdapter.refillList(users)
    }

    private fun initializeRecyclerView() {
        searchAdapter = UserSearchAdapter(requireActivity(), searchViewModel.usersList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
            setDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.divider)!!)
        }
        recyclerView.addItemDecoration(dividerItemDecoration)

        recyclerView.adapter = searchAdapter
    }

    private fun setupEditView() {
        searchField.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                search(text.toString()) {
                    showProgress(false)
                    emptyText.visibility = if (searchViewModel.usersList.isEmpty()) VISIBLE else GONE
                    updateList(searchViewModel.usersList)
                }
            } else {
                searchViewModel.usersList.clear()
                updateList(searchViewModel.usersList)
            }
        }
    }

    private fun openLogoutDialog() {
        LogoutDialog(this).start()
    }

    private fun showProgress(show: Boolean) {
        progressLayout.visibility = if (show) VISIBLE else GONE
    }
}
