package com.yuta.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.app.R
import com.yuta.common.ui.BaseFragment
import com.yuta.domain.model.UserDto
import com.yuta.search.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment() {

    private val emptyText: View by lazy { requireView().findViewById(R.id.empty_text) }
    private val searchField: EditText by lazy { requireView().findViewById(R.id.search_user) }
    private val recyclerView: RecyclerView by lazy { requireView().findViewById(R.id.search_list) }

    private lateinit var searchAdapter: UserSearchAdapter

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false).also {
            initializeRecyclerView()
            setupEditView()
        }
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
}
