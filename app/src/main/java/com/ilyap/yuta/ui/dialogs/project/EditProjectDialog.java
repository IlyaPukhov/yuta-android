package com.ilyap.yuta.ui.dialogs.project;

import android.content.Context;
import androidx.fragment.app.Fragment;
import com.ilyap.yuta.models.Project;

@SuppressWarnings("ConstantConditions")
public class EditProjectDialog extends CreateProjectDialog {
    private final int projectId;
    private Project project;

    public EditProjectDialog(Context context, Fragment fragment, int projectId) {
        super(context, fragment);
        this.projectId = projectId;
    }

    @Override
    public void start() {
        super.start();
//        setupViews();
//        getProject(teamId);
//
//        // EDIT String status = spinner.getSelectedItem().toString();
//

        submitButton.setEnabled(false);
        submitButton.setOnClickListener(v -> {
            hideKeyboard(submitButton);
//            editTeam();
        });
    }

//    private void setupViews() {
//        submitButton.setText(R.string.save_button);
//        ((TextView) dialog.findViewById(R.id.create_text)).setText(getContext().getString(R.string.edit_team_text));
//    }
//
//    private void getTeam(int teamId) {
//        viewModel.getResultLiveData().removeObservers(fragment);
//        viewModel.getTeam(teamId);
//        viewModel.getResultLiveData().observe(fragment, result -> {
//            if (!(result instanceof Team)) return;
//            team = (Team) result;
//            setupTeam();
//        });
//    }
//
//    private void setupTeam() {
//        teamName.setText(team.getName());
//        membersAdapter.updateList(team.getMembers());
//        updateAddedTextVisibility();
//    }
//
//    private void editTeam() {
//        viewModel.getResultLiveData().removeObservers(fragment);
//        viewModel.editTeam(teamId, getData(teamName), addedMembers);
//        viewModel.getResultLiveData().observe(fragment, result -> {
//            if (!(result instanceof UpdateResponse)) return;
//            ((TeamsFragment) fragment).updateCarousels();
//            dismiss();
//        });
//    }

//    private void statusSpinnerInitialize() {
//        spinner = findViewById(R.id.status);
//        ArrayAdapter<ProjectStatus> adapter = new ArrayAdapter<>(
//                getContext(),
//                android.R.layout.simple_spinner_item,
//                ProjectStatus.values()
//        );
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setSelection(0);

//    }
}