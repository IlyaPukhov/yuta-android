package com.yuta.projects.ui.dialog

import android.widget.Button
import androidx.fragment.app.Fragment
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.domain.model.ProjectDto

class ProjectMenuDialog(
    private val project: ProjectDto,
    private val fragment: Fragment,
    private val onUpdateCallback: () -> Unit
) : CancelableDialog(R.layout.dialog_project_menu, fragment.requireActivity()) {

    private val editProjectButton: Button by lazy { dialog.findViewById(R.id.edit_project) }
    private val deleteProjectButton: Button by lazy { dialog.findViewById(R.id.delete_project) }

    override fun start() {
        super.start()

        editProjectButton.setOnClickListener {
            openEditProjectDialog()
            dismiss()
        }
        deleteProjectButton.setOnClickListener {
            openDeleteProjectDialog()
            dismiss()
        }
    }

    private fun openEditProjectDialog() = EditProjectDialog(project, fragment) { onUpdateCallback() }.start()

    private fun openDeleteProjectDialog() = DeleteProjectDialog(project, fragment) { onUpdateCallback() }.start()
}
