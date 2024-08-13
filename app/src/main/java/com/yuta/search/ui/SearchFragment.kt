package com.yuta.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.__old.ui.dialog.user.LogoutDialog
import com.yuta.app.R
import com.yuta.domain.model.UserDto
import com.yuta.search.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var emptyText: View
    private lateinit var progressLayout: View
    private lateinit var searchAdapter: UserSearchAdapter
    private var usersList: MutableList<UserDto> = mutableListOf()
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        emptyText = view.findViewById(R.id.empty_text)
        progressLayout = view.findViewById(R.id.progressLayout)
        val searchField = view.findViewById<EditText>(R.id.search_user)

        recyclerViewInitialize(view)
        searchField.addTextChangedListener(searchTextWatcher())

        view.findViewById<View>(R.id.log_out).setOnClickListener { openLogoutDialog() }

        return view
    }

    override fun onResume() {
        super.onResume()
        updateList(usersList)
    }

    private fun searchTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (count != 0) {
                updateSearchResult(s.toString())
            } else {
                usersList.clear()
                updateList(usersList)
            }
        }
    }

    private fun updateSearchResult(searchName: String) {
        progressLayout.visibility = VISIBLE

        viewModel.viewModelScope.launch {
            viewModel.search(searchName).collect { list ->
                progressLayout.visibility = GONE
                usersList = list.toMutableList()

                emptyText.visibility = if (usersList.isEmpty()) VISIBLE else GONE
                updateList(usersList)
            }
        }
    }

    private fun updateList(users: List<UserDto>) {
        searchAdapter.refillList(users)
    }

    private fun recyclerViewInitialize(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.search_list)
        searchAdapter = UserSearchAdapter(requireActivity(), usersList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
            setDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.divider)!!)
        }
        recyclerView.addItemDecoration(dividerItemDecoration)

        recyclerView.adapter = searchAdapter
    }

    private fun openLogoutDialog() {
        val logoutDialog = LogoutDialog(requireContext(), this)
        logoutDialog.start()
    }
}
