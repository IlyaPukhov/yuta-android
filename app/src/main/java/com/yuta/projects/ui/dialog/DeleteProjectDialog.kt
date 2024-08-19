package com.yuta.projects.ui.dialog

import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.domain.model.ProjectDto
import com.yuta.projects.viewmodel.ProjectDialogsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.getValue

class DeleteProjectDialog(
    private val project: ProjectDto,
    fragment: Fragment,
    private val onDeleteSuccessCallback: () -> Unit
) : CancelableDialog(R.layout.dialog_delete, fragment.requireActivity()) {

    private val submitButton: Button by lazy { dialog.findViewById(R.id.submit) }
    private val closeButton: Button by lazy { dialog.findViewById(R.id.close) }
    private val description: TextView by lazy { dialog.findViewById(R.id.desc) }

    private val projectViewModel: ProjectDialogsViewModel by fragment.viewModels()

    override fun start() {
        super.start()
        setupTextView(project.name)

        closeButton.setOnClickListener { dismiss() }
        submitButton.setOnClickListener { deleteProject(project.id) }
    }

    private fun setupTextView(name: String) {
        val text = "${context.getString(R.string.delete_project_desc)} \"$name\"?"
        description.text = text
    }

    private fun deleteProject(id: Int) {
        projectViewModel.viewModelScope.launch {
            val isDeleted = projectViewModel.delete(id).first()
            handleDeleteResult(isDeleted)
        }
    }

    private fun handleDeleteResult(isDeleted: Boolean) {
        if (isDeleted) {
            onDeleteSuccessCallback()
            dismiss()
        }
    }
}
