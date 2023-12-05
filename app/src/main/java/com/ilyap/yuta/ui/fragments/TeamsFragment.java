package com.ilyap.yuta.ui.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.models.TeamMember;
import com.ilyap.yuta.models.TeamResponse;
import com.ilyap.yuta.ui.adapters.CarouselAdapter;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.team.CreateTeamDialog;
import com.ilyap.yuta.utils.RequestViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeamsFragment extends Fragment {
    private ToggleButton managedTeamsButton;
    private ToggleButton memberTeamsButton;
    private TextView emptyText;
    private View progressLayout;
    private View view;
    private RequestViewModel viewModel;
    private CarouselAdapter carouselAdapter;

    public TeamsFragment() {
    }

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

        view.findViewById(R.id.create_team).setOnClickListener(v -> openCreateTeamDialog());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCarousels();
    }

    private void fillCarousels(List<Team> teams) {
        emptyText.setVisibility(teams.isEmpty() ? VISIBLE : GONE);

        carouselAdapter.updateList(teams.stream()
                .map(team -> {
                    List<TeamMember> membersList = new ArrayList<>();
                    membersList.add(new TeamMember(team, team.getLeader()));
                    membersList.addAll(team.getMembers().stream()
                            .map(member -> new TeamMember(team, member))
                            .collect(Collectors.toList()));

                    return membersList;
                })
                .collect(Collectors.toList()));
    }

    private void recyclerViewInitialize() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<List<TeamMember>> carouselList = new ArrayList<>();
        carouselAdapter = new CarouselAdapter(requireContext(), carouselList, this);
        recyclerView.setAdapter(carouselAdapter);
    }

    private void teamsSwitchInitialize() {
        managedTeamsButton = view.findViewById(R.id.manager_button);
        memberTeamsButton = view.findViewById(R.id.member_button);
        managedTeamsButton.setOnClickListener(this::onToggleButtonClick);
        memberTeamsButton.setOnClickListener(this::onToggleButtonClick);
    }

    private void loadTeams(View button) {
        viewModel.getResultLiveData().removeObservers(getViewLifecycleOwner());
        viewModel.getTeams(getUserId(requireActivity()));
        viewModel.getResultLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!(result instanceof TeamResponse)) return;
            progressLayout.setVisibility(GONE);
            TeamResponse teamResponse = (TeamResponse) result;

            if (button.getId() == managedTeamsButton.getId()) {
                fillCarousels(teamResponse.getManagedTeams());
            } else {
                fillCarousels(teamResponse.getOthersTeams());
            }
        });
    }

    private void onToggleButtonClick(View view) {
        final ToggleButton button = (ToggleButton) view;
        final ToggleButton otherButton;

        if (button.getId() == managedTeamsButton.getId()) {
            otherButton = memberTeamsButton;
        } else {
            otherButton = managedTeamsButton;
        }

        button.setTextAppearance(R.style.active_teams);
        button.setChecked(true);
        otherButton.setTextAppearance(R.style.default_teams);
        otherButton.setChecked(false);

        loadTeams(button);
    }

    public void updateCarousels() {
        managedTeamsButton.performClick();
    }

    private void openCreateTeamDialog() {
        CustomDialog createTeamDialog = new CreateTeamDialog(view.getContext(), this);
        createTeamDialog.start();
    }
}