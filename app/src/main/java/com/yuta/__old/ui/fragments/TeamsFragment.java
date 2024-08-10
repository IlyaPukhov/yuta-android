package com.yuta.__old.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yuta.__old.R;
import com.yuta.__old.ui.adapters.TeamsAdapter;
import com.yuta.__old.ui.dialogs.CustomDialog;
import com.yuta.__old.ui.dialogs.team.CreateTeamDialog;
import com.yuta.__old.ui.dialogs.user.LogoutDialog;
import com.yuta.app.network.RequestViewModel;
import com.yuta.app.domain.model.entity.Team;
import com.yuta.app.domain.model.entity.TeamMember;
import com.yuta.app.domain.model.response.TeamsResponse;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.yuta.app.util.UserUtils.getUserId;

@NoArgsConstructor
public class TeamsFragment extends Fragment {
    private static int lastPickedButtonId;
    private ToggleButton managedTeamsButton;
    private ToggleButton memberTeamsButton;
    private TextView emptyText;
    private View progressLayout;
    private View view;
    private RequestViewModel viewModel;
    private TeamsAdapter teamsAdapter;
    private List<List<TeamMember>> managedTeamsMembers;
    private List<List<TeamMember>> othersTeamsMembers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teams, container, false);

        emptyText = view.findViewById(R.id.empty_text);
        progressLayout = view.findViewById(R.id.progressLayout);
        progressLayout.setVisibility(VISIBLE);

        viewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        recyclerViewInitialize();
        teamsSwitchInitialize();

        if (lastPickedButtonId == 0) {
            lastPickedButtonId = memberTeamsButton.getId();
        }

        view.findViewById(R.id.create_team).setOnClickListener(v -> openCreateTeamDialog());
        view.findViewById(R.id.log_out).setOnClickListener(v -> openLogoutDialog());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateLists();
    }

    private void fillTeams(@NonNull List<List<TeamMember>> teamMembers) {
        emptyText.setVisibility(teamMembers.isEmpty() ? VISIBLE : GONE);
        teamsAdapter.updateList(teamMembers);
    }

    private void getTeams() {
        viewModel.getResultLiveData().removeObservers(getViewLifecycleOwner());
        viewModel.getTeams(getUserId(requireActivity()));
        viewModel.getResultLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!(result instanceof TeamsResponse)) return;
            progressLayout.setVisibility(GONE);
            TeamsResponse teamsResponse = (TeamsResponse) result;
            managedTeamsMembers = getTeamMembers(teamsResponse.getManagedTeams());
            othersTeamsMembers = getTeamMembers(teamsResponse.getOthersTeams());
        });
    }

    public void updateLists() {
        updateLists(view.findViewById(lastPickedButtonId));
    }

    private void updateLists(Button button) {
        getTeams();
        viewModel.getResultLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!(result instanceof TeamsResponse)) return;
            openTab(button);
        });
    }

    private void openTab(@NonNull Button button) {
        button.performClick();
    }

    private void onToggleButtonClick(View view) {
        final ToggleButton button = (ToggleButton) view;
        final ToggleButton otherButton;

        if (button.getId() == managedTeamsButton.getId()) {
            otherButton = memberTeamsButton;
            fillTeams(managedTeamsMembers);
        } else {
            otherButton = managedTeamsButton;
            fillTeams(othersTeamsMembers);
        }

        button.setTextAppearance(R.style.active_toggle);
        button.setChecked(true);
        otherButton.setTextAppearance(R.style.default_toggle);
        otherButton.setChecked(false);
        lastPickedButtonId = button.getId();
    }

    private List<List<TeamMember>> getTeamMembers(List<Team> teams) {
        return teams.stream()
                .map(team -> {
                    List<TeamMember> membersList = new ArrayList<>();
                    membersList.add(new TeamMember(team, team.getLeader()));
                    membersList.addAll(team.getMembers().stream()
                            .map(member -> new TeamMember(team, member))
                            .collect(Collectors.toList()));

                    return membersList;
                })
                .collect(Collectors.toList());
    }

    private void recyclerViewInitialize() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<List<TeamMember>> teamList = new ArrayList<>();
        teamsAdapter = new TeamsAdapter(requireActivity(), teamList, this);
        recyclerView.setAdapter(teamsAdapter);
    }

    private void teamsSwitchInitialize() {
        managedTeamsButton = view.findViewById(R.id.manager_button);
        memberTeamsButton = view.findViewById(R.id.member_button);
        managedTeamsButton.setOnClickListener(this::onToggleButtonClick);
        memberTeamsButton.setOnClickListener(this::onToggleButtonClick);
    }

    private void openCreateTeamDialog() {
        CustomDialog createTeamDialog = new CreateTeamDialog(view.getContext(), this);
        createTeamDialog.start();
    }

    private void openLogoutDialog() {
        CustomDialog logoutDialog = new LogoutDialog(view.getContext(), this);
        logoutDialog.start();
    }
}