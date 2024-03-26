package com.ilyap.yuta.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.ProjectDto;
import com.ilyap.yuta.models.ProjectsResponse;
import com.ilyap.yuta.ui.adapters.ProjectsAdapter;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.project.CreateProjectDialog;
import com.ilyap.yuta.ui.dialogs.user.LogoutDialog;
import com.ilyap.yuta.utils.RequestViewModel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.ui.dialogs.project.CreateProjectDialog.PICK_PDF_REQUEST;
import static com.ilyap.yuta.utils.UserUtils.getUserId;

@NoArgsConstructor
public class ProjectsFragment extends Fragment {
    private static int lastPickedButtonId;
    public final ActivityResultLauncher<Intent> pdfPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    CreateProjectDialog.handleActivityResult(PICK_PDF_REQUEST, Activity.RESULT_OK, result.getData());
                }
            }
    );
    private ToggleButton managedProjectsButton;
    private ToggleButton memberProjectsButton;
    private TextView emptyText;
    private View progressLayout;
    private View view;
    private RequestViewModel viewModel;
    private List<ProjectDto> managedProjectsMembers;
    private List<ProjectDto> othersProjectsMembers;
    private ProjectsAdapter projectsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_projects, container, false);

        emptyText = view.findViewById(R.id.empty_text);
        progressLayout = view.findViewById(R.id.progressLayout);
        progressLayout.setVisibility(VISIBLE);

        viewModel = new ViewModelProvider(this).get(RequestViewModel.class);

        recyclerViewInitialize();
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
        updateLists();
    }

    private void fillProjects(@NonNull List<ProjectDto> projectMembers) {
        emptyText.setVisibility(projectMembers.isEmpty() ? VISIBLE : GONE);
        projectsAdapter.updateList(projectMembers);
    }

    private void getProjects() {
        viewModel.getResultLiveData().removeObservers(getViewLifecycleOwner());
        viewModel.getProjects(getUserId(requireActivity()));
        viewModel.getResultLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!(result instanceof ProjectsResponse)) return;
            progressLayout.setVisibility(GONE);
            ProjectsResponse projectsResponse = (ProjectsResponse) result;
            managedProjectsMembers = projectsResponse.getManagedProjects();
            othersProjectsMembers = projectsResponse.getOthersProjects();
        });
    }

    public void updateLists() {
        updateLists(view.findViewById(lastPickedButtonId));
    }

    private void updateLists(Button button) {
        getProjects();
        viewModel.getResultLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!(result instanceof ProjectsResponse)) return;
            openTab(button);
        });
    }

    private void openTab(@NonNull Button button) {
        button.performClick();
    }

    private void recyclerViewInitialize() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<ProjectDto> projectList = new ArrayList<>();
        projectsAdapter = new ProjectsAdapter(requireActivity(), projectList, this);
        recyclerView.setAdapter(projectsAdapter);
    }

    private void onToggleButtonClick(View view) {
        final ToggleButton button = (ToggleButton) view;
        final ToggleButton otherButton;

        if (button.getId() == managedProjectsButton.getId()) {
            otherButton = memberProjectsButton;
            fillProjects(managedProjectsMembers);
        } else {
            otherButton = managedProjectsButton;
            fillProjects(othersProjectsMembers);
        }

        button.setTextAppearance(R.style.active_toggle);
        button.setChecked(true);
        otherButton.setTextAppearance(R.style.default_toggle);
        otherButton.setChecked(false);
        lastPickedButtonId = button.getId();
    }

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