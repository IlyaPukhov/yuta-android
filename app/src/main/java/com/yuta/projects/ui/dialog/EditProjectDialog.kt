package com.yuta.projects.ui.dialog

import android.view.View
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.yuta.app.R
import com.yuta.common.util.DateUtils
import com.yuta.common.util.FieldUtils.getData
import com.yuta.common.util.FileUtils
import com.yuta.common.util.KeyboardUtils
import com.yuta.domain.model.ProjectDto
import com.yuta.domain.model.ProjectStatus
import com.yuta.domain.model.Team
import com.yuta.projects.viewmodel.ProjectDialogsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditProjectDialog(
    private val projectDto: ProjectDto,
    private val fragment: Fragment,
    private val onEditSuccessCallback: () -> Unit
) : CreateProjectDialog(fragment) {

    companion object {
        private val RADIO_CREATE_WITH_TEAM: Int = R.id.create_with_team
    }

    private val spinner: Spinner by lazy { dialog.findViewById(R.id.status) }
    private val title: TextView by lazy { dialog.findViewById(R.id.title) }
    private val statusContainer: View by lazy { dialog.findViewById(R.id.status_container) }

    private val projectViewModel: ProjectDialogsViewModel by fragment.viewModels()

    override fun start() {
        super.start()
        setupViews()
        initializeStatusSpinner()
        populateProjectDetails()

        submitButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), submitButton)
            editProject()
        }
    }

    private fun setupViews() {
        submitButton.apply {
            setText(R.string.save_button)
            isEnabled = false
        }
        title.setText(R.string.edit_project_text)
        statusContainer.visibility = VISIBLE
    }

    private fun populateProjectDetails() {
        val teamList = mutableListOf<Team>()

        projectViewModel.viewModelScope.launch {
            val project = projectViewModel.getById(projectDto.id).first()

            project.team?.let {
                radioGroup.check(RADIO_CREATE_WITH_TEAM)
                teamList.add(it)
                addedTeamSearchAdapter.refillList(teamList)
            }

            fileName.text = project.technicalTaskName
            deadlineField.text = DateUtils.formatToEuropean(project.deadline)
            projectName.setText(project.name)
            projectDesc.setText(project.description)
            spinner.setSelection(ProjectStatus.fromText(project.status).ordinal)

            updateAddedTextVisibility()
        }
    }

    private fun editProject() {
        projectViewModel.viewModelScope.launch {
            val result = projectViewModel.edit(
                id = projectDto.id,
                name = projectName.getData().toString(),
                description = projectDesc.getData().toString(),
                deadline = DateUtils.formatToIso(deadlineField.getData().toString()),
                teamId = projectViewModel.addedTeams.firstOrNull()?.id,
                status = spinner.selectedItem.toString(),
                filename = FileUtils.getFilename(fragment.requireContext(), techTaskUri),
                technicalTask = techTaskUri?.let { context.contentResolver.openInputStream(it) }
            ).first()

            if (result) {
                onEditSuccessCallback()
                dismiss()
            }
        }
    }

    private fun initializeStatusSpinner() {
        spinner.apply {
            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                ProjectStatus.entries.toTypedArray()
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position != ProjectStatus.fromText(projectDto.status).ordinal) {
                        updateSubmitButtonState()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }
}
