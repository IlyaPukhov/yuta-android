package com.ilyap.yuta.ui.dialogs.project;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.Project;
import com.ilyap.yuta.models.ProjectResponse;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.ui.fragments.ProjectsFragment;
import com.ilyap.yuta.utils.ProjectStatus;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class EditProjectDialog extends CreateProjectDialog {
    private final int projectId;
    private Project project;
    private Spinner spinner;

    public EditProjectDialog(Context context, Fragment fragment, int projectId) {
        super(context, fragment);
        this.projectId = projectId;
    }

    @Override
    public void start() {
        super.start();
        statusSpinnerInitialize();
        setupViews();
        getProject(projectId);

        submitButton.setEnabled(false);
        submitButton.setOnClickListener(v -> {
            hideKeyboard(submitButton);
            editProject();
        });
    }

    private void setupViews() {
        submitButton.setText(R.string.save_button);
        ((TextView) dialog.findViewById(R.id.create_text)).setText(getContext().getString(R.string.edit_project));
    }

    private void getProject(int projectId) {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.getProject(projectId);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof ProjectResponse)) return;
            project = ((ProjectResponse) result).getProject();
            setupProject();
        });
    }

    private void setupProject() {
        List<Team> teamList = new ArrayList<>();
        if (project.getTeam() != null) {
            teamList.add(project.getTeam());
        }
        teamSearchAdapter.updateList(teamList);

        projectName.setText(project.getName());
        projectDesc.setText(project.getDescription());
        fileName.setText(project.getTechnicalTaskName());
        spinner.setSelection(ProjectStatus.valueOf(project.getStatus()).ordinal());
        deadlineField.setText(getUnformattedDate(project.getDeadline()));

        updateAddedTextVisibility();
    }

    @SneakyThrows
    private void editProject() {
        String name = getData(projectName);
        String description = getData(projectDesc);
        String deadline = getFormattedDate(getData(deadlineField));
        String status = spinner.getSelectedItem().toString();
        InputStream techTaskInputStream = techTaskUri != null ? fragment.requireContext().getContentResolver().openInputStream(techTaskUri) : null;
        int teamId = getCurrentTeamId();

        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.editProject(projectId, name, description, deadline, getData(fileName), status, techTaskInputStream, teamId);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            ((ProjectsFragment) fragment).updateLists();
            dismiss();
        });
    }

    private String getUnformattedDate(String date) {
        DateTimeFormatter sourceDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, sourceDateFormatter);

        return localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private void statusSpinnerInitialize() {
        spinner = dialog.findViewById(R.id.status);
        ArrayAdapter<ProjectStatus> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                ProjectStatus.values()
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}