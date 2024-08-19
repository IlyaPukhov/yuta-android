package com.yuta.projects.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.app.R
import com.yuta.common.ui.BaseFragment
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.ProjectDto
import com.yuta.projects.ui.adapter.ProjectsAdapter
import com.yuta.projects.ui.dialog.CreateProjectDialog
import com.yuta.projects.viewmodel.ProjectsViewModel
import kotlinx.coroutines.launch

class ProjectsFragment : BaseFragment() {

    private val createProjectButton: Button by lazy { requireView().findViewById(R.id.create_project) }
    private val managedProjectsButton: ToggleButton by lazy { requireView().findViewById(R.id.manager_button) }
    private val memberProjectsButton: ToggleButton by lazy { requireView().findViewById(R.id.member_button) }
    private val emptyText: TextView by lazy { requireView().findViewById(R.id.empty_text) }
    private val projectsAdapter: ProjectsAdapter by lazy {
        ProjectsAdapter(mutableListOf(), this) { updateLists() }
    }

    private val projectsViewModel: ProjectsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_projects, container, false).also {
            setupRecyclerView()
            setupToggleButtons()
            setupViews()
        }
    }

    override fun onStart() {
        super.onStart()
        updateLists()
    }

    private fun updateLists() {
        projectsViewModel.lastPickedButtonId?.let {
            updateLists(requireView().findViewById(it))
        }
    }

    private fun updateLists(button: View) {
        showProgress(true)
        updateProjects {
            button.performClick()
            showProgress(false)
        }
    }

    private fun updateProjects(onUpdateCallback: () -> Unit) {
        projectsViewModel.viewModelScope.launch {
            projectsViewModel.getAll(UserUtils.getUserId(requireContext())).collect {
                projectsViewModel.initializeProjects(it)
                onUpdateCallback()
            }
        }
    }

    private fun fillProjects(projectMembers: List<ProjectDto>) {
        emptyText.visibility = if (projectMembers.isEmpty()) VISIBLE else GONE
        projectsAdapter.refillList(projectMembers)
    }

    private fun onToggleButtonClick(view: View) {
        val button = view as ToggleButton
        val otherButton = if (button.id == managedProjectsButton.id) memberProjectsButton else managedProjectsButton

        if (button.id == managedProjectsButton.id) {
            fillProjects(projectsViewModel.managedProjectsMembers)
        } else {
            fillProjects(projectsViewModel.othersProjectsMembers)
        }

        setActiveButton(button)
        setInactiveButton(otherButton)
        projectsViewModel.lastPickedButtonId = button.id
    }

    private fun setupViews() {
        createProjectButton.setOnClickListener { openCreateProjectDialog() }
    }

    private fun setupRecyclerView() {
        requireView().findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = projectsAdapter
        }
    }

    private fun setupToggleButtons() {
        managedProjectsButton.setOnClickListener(this::onToggleButtonClick)
        memberProjectsButton.setOnClickListener(this::onToggleButtonClick)

        projectsViewModel.lastPickedButtonId = projectsViewModel.lastPickedButtonId ?: memberProjectsButton.id
    }

    private fun openCreateProjectDialog() = CreateProjectDialog(fragment = this) { updateLists() }.start()

    private fun setActiveButton(button: ToggleButton) {
        button.setTextAppearance(R.style.active_toggle)
        button.isChecked = true
    }

    private fun setInactiveButton(button: ToggleButton) {
        button.setTextAppearance(R.style.default_toggle)
        button.isChecked = false
    }
}
