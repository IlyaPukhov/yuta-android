package com.yuta.__old.ui.dialog.project;

import android.content.Context;
import androidx.fragment.app.Fragment;
import com.yuta.__old.R;
import com.yuta.app.domain.model.entity.ProjectDto;
import com.yuta.common.ui.CustomDialog;
import com.yuta.common.ui.CustomInteractiveDialog;

@SuppressWarnings("ConstantConditions")
public class ProjectDialog extends CustomInteractiveDialog {
    private final ProjectDto project;

    public ProjectDialog(Context context, Fragment fragment, ProjectDto project) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_project);
        this.project = project;
    }

    @Override
    public void start() {
        super.start();

        dialog.findViewById(R.id.edit_project).setOnClickListener(v -> {
            openEditProjectDialog();
            dismiss();
        });
        dialog.findViewById(R.id.delete_project).setOnClickListener(v -> {
            openDeleteProjectDialog();
            dismiss();
        });
    }

    private void openEditProjectDialog() {
        CustomDialog editProjectDialog = new EditProjectDialog(activity, fragment, project.getId());
        editProjectDialog.start();
    }

    private void openDeleteProjectDialog() {
        CustomDialog deleteProjectDialog = new DeleteProjectDialog(activity, fragment, project);
        deleteProjectDialog.start();
    }
}