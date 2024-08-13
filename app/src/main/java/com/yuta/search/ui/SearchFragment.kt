package com.yuta.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.__old.R
import com.yuta.app.domain.model.entity.User
import com.yuta.app.domain.model.response.SearchUsersResponse
import com.yuta.app.network.RequestViewModel
import com.yuta.common.ui.CustomDialog
import com.yuta.__old.ui.dialog.user.LogoutDialog

class SearchFragment : Fragment() {

    private lateinit var emptyText: View
    private lateinit var progressLayout: View
    private lateinit var searchAdapter: UserSearchAdapter
    private var userDto: MutableList<User> = mutableListOf()
    private val viewModel: RequestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        emptyText = view.findViewById(R.id.empty_text)
        progressLayout = view.findViewById(R.id.progressLayout)
        val searchField = view.findViewById<EditText>(R.id.search_user)

        recyclerViewInitialize(view)
        setupEditView(searchField)

        view.findViewById<View>(R.id.log_out).setOnClickListener { openLogoutDialog() }

        return view
    }

    override fun onResume() {
        super.onResume()
        updateList(userDto)
    }

    private fun setupEditView(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count != 0) {
                    updateSearchResult(s.toString())
                } else {
                    userDto.clear()
                    updateList(userDto)
                }
            }
        })
    }

    private fun updateSearchResult(searchText: String) {
        progressLayout.visibility = View.VISIBLE
        viewModel.resultLiveData.removeObservers(viewLifecycleOwner)
        viewModel.searchUsers(searchText)
        viewModel.resultLiveData.observe(viewLifecycleOwner) { result ->
            if (result is SearchUsersResponse) {
                progressLayout.visibility = View.GONE
                userDto = result.usersDtos.toMutableList()
                emptyText.visibility = if (userDto.isEmpty()) View.VISIBLE else View.GONE
                updateList(userDto)
            }
        }
    }

    private fun updateList(userDto: List<User>) {
        searchAdapter.refillList(userDto)
    }

    private fun recyclerViewInitialize(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.search_list)
        searchAdapter = UserSearchAdapter(requireActivity(), userDto)
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
