package com.ilyap.yuta.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.ilyap.yuta.R;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.project.CreateProjectDialog;
import com.ilyap.yuta.ui.dialogs.user.LogoutDialog;
import lombok.NoArgsConstructor;

import static com.ilyap.yuta.ui.dialogs.project.CreateProjectDialog.PICK_PDF_REQUEST;

@NoArgsConstructor
public class ProjectsFragment extends Fragment {
    private ToggleButton managedProjectsButton;
    private ToggleButton memberProjectsButton;
    private View view;
    private static int lastPickedButtonId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_projects, container, false);

        projectsSwitchInitialize();

        if (lastPickedButtonId == 0) {
            lastPickedButtonId = memberProjectsButton.getId();
        }

        view.findViewById(R.id.create_project).setOnClickListener(v -> openCreateProjectDialog());
        view.findViewById(R.id.log_out).setOnClickListener(v -> openLogoutDialog());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        updateCarousels();
    }


    private void openTab(@NonNull Button button) {
        button.performClick();
    }

    private void onToggleButtonClick(View view) {
        final ToggleButton button = (ToggleButton) view;
        final ToggleButton otherButton;

        if (button.getId() == managedProjectsButton.getId()) {
            otherButton = memberProjectsButton;
//            fillCarousels(managedTeamsMembers);
        } else {
            otherButton = managedProjectsButton;
//            fillCarousels(othersTeamsMembers);
        }

        button.setTextAppearance(R.style.active_toggle);
        button.setChecked(true);
        otherButton.setTextAppearance(R.style.default_toggle);
        otherButton.setChecked(false);
        lastPickedButtonId = button.getId();
    }

    public final ActivityResultLauncher<Intent> pdfPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    CreateProjectDialog.handleActivityResult(PICK_PDF_REQUEST, Activity.RESULT_OK, result.getData());
                }
            }
    );

    private void projectsSwitchInitialize() {
        managedProjectsButton = view.findViewById(R.id.manager_button);
        memberProjectsButton = view.findViewById(R.id.member_button);
        managedProjectsButton.setOnClickListener(this::onToggleButtonClick);
        memberProjectsButton.setOnClickListener(this::onToggleButtonClick);
    }

    private void openCreateProjectDialog() {
        CustomDialog createProjectDialog = new CreateProjectDialog(view.getContext(), this);
        createProjectDialog.start();
    }

    private void openLogoutDialog() {
        CustomDialog logoutDialog = new LogoutDialog(view.getContext(), this);
        logoutDialog.start();
    }
}