package com.ilyap.yuta.ui.dialogs.project;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.Project;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.utils.RequestViewModel;

@SuppressWarnings("ConstantConditions")
public class DeleteProjectDialog extends CustomInteractiveDialog {
    private RequestViewModel viewModel;
    private final Project project;

    public DeleteProjectDialog(Context context, Fragment fragment, Project project) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_delete);
        this.project = project;
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);
        setupTextView(project.getName());

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> {
            deleteProject(fragment, project);
            dismiss();
        });
    }

    private void setupTextView(String name) {
        String text = getContext().getString(R.string.delete_project_desc) + " \"" + name + "\"?";
        ((TextView) dialog.findViewById(R.id.name_desc)).setText(text);
    }

    private void deleteProject(Fragment fragment, @NonNull Project project) {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.deleteTeam(project.getId());
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
//            ((ProjectsFragment) fragment).updateProjectsList();
            dismiss();
        });
    }
}