package com.yuta.projects.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.app.R
import com.yuta.common.ui.BaseFragment
import com.yuta.common.util.UserUtils.getUserId
import com.yuta.projects.ui.adapter.ProjectsAdapter
import com.yuta.projects.ui.dialog.CreateProjectDialog

class ProjectsFragment : BaseFragment() {

    private lateinit var pdfPickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var managedProjectsButton: ToggleButton
    private lateinit var memberProjectsButton: ToggleButton
    private lateinit var emptyText: TextView
    private lateinit var progressLayout: View
    private lateinit var projectsAdapter: ProjectsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_projects, container, false).also {
            setupRecyclerView(it)
            setupToggleButtons()
            setupViews()
        }

        emptyText = requireView().findViewById(R.id.empty_text)
        progressLayout = requireView().findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        viewModel = ViewModelProvider(this).get(RequestViewModel::class.java)

        recyclerViewInitialize()
        projectsSwitchInitialize()

        if (lastPickedButtonId == 0) {
            lastPickedButtonId = memberProjectsButton.id
        }

        requireView().findViewById<Button>(R.id.create_project).setOnClickListener { openCreateProjectDialog() }
        requireView().findViewById<Button>(R.id.log_out).setOnClickListener { openLogoutDialog() }

        pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                CreateProjectDialog.handleActivityResult(
                    CreateProjectDialog.PICK_PDF_REQUEST,
                    Activity.RESULT_OK,
                    result.data
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        updateLists()
    }

    private fun fillProjects(projectMembers: List<ProjectDto>) {
        emptyText.visibility = if (projectMembers.isEmpty()) View.VISIBLE else View.GONE
        projectsAdapter.refillList(projectMembers)
    }

    private fun getProjects() {
        viewModel.getResultLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getProjects(getUserId(requireActivity()))
        viewModel.getResultLiveData().observe(viewLifecycleOwner) { result ->
            if (result is ProjectsResponse) {
                progressLayout.visibility = View.GONE
                managedProjectsMembers = result.managedProjects
                othersProjectsMembers = result.othersProjects
            }
        }
    }

    private fun updateLists() {
        updateLists(requireView().findViewById(lastPickedButtonId))
    }

    private fun updateLists(button: View) {
        getProjects()
        viewModel.getResultLiveData().observe(viewLifecycleOwner) { result ->
            if (result is ProjectsResponse) {
                openTab(button as Button)
            }
        }
    }

    private fun openTab(button: Button) {
        button.performClick()
    }

    private fun recyclerViewInitialize() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        projectsAdapter = ProjectsAdapter(requireActivity(), listOf(), this)
        recyclerView.adapter = projectsAdapter
    }

    private fun onToggleButtonClick(view: View) {
        val button = view as ToggleButton
        val otherButton: ToggleButton

        if (button.id == managedProjectsButton.id) {
            otherButton = memberProjectsButton
            fillProjects(managedProjectsMembers)
        } else {
            otherButton = managedProjectsButton
            fillProjects(othersProjectsMembers)
        }

        button.setTextAppearance(R.style.active_toggle)
        button.isChecked = true
        otherButton.setTextAppearance(R.style.default_toggle)
        otherButton.isChecked = false
        lastPickedButtonId = button.id
    }

    private fun projectsSwitchInitialize() {
        managedProjectsButton = requireView().findViewById(R.id.manager_button)
        memberProjectsButton = requireView().findViewById(R.id.member_button)
        managedProjectsButton.setOnClickListener(this::onToggleButtonClick)
        memberProjectsButton.setOnClickListener(this::onToggleButtonClick)
    }

    private fun openCreateProjectDialog() = CreateProjectDialog(fragment = this) { updateLists() }.start()
}
