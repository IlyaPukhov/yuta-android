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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.models.TeamMember;
import com.ilyap.yuta.models.TeamResponse;
import com.ilyap.yuta.ui.carousel.CarouselAdapter;
import com.ilyap.yuta.utils.JsonUtils;
import com.ilyap.yuta.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeamsFragment extends Fragment {
    private ToggleButton managedTeamsButton;
    private ToggleButton memberTeamsButton;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private View view;
    private int userId;

    public TeamsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teams, container, false);
        emptyText = view.findViewById(R.id.empty_text);
        userId = getUserId(requireActivity());
        recyclerViewInitialize();

        teamsSwitchInitialize();
        return view;
    }

    private void fillCarousels(List<Team> teams) {
        if (teams.isEmpty()) {
            emptyText.setVisibility(VISIBLE);
            return;
        }

        emptyText.setVisibility(GONE);
        List<List<TeamMember>> carouselList = teams.stream()
                .map(team -> {
                    List<TeamMember> membersList = new ArrayList<>();
                    membersList.add(new TeamMember(team, team.getLeader()));
                    membersList.addAll(team.getMembers().stream()
                            .map(member -> new TeamMember(team, member))
                            .collect(Collectors.toList()));

                    return membersList;
                })
                .collect(Collectors.toList());

        CarouselAdapter carouselAdapter = new CarouselAdapter(carouselList, requireContext());
        recyclerView.setAdapter(carouselAdapter);
    }

    private void recyclerViewInitialize() {
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void teamsSwitchInitialize() {
        managedTeamsButton = view.findViewById(R.id.manager_button);
        memberTeamsButton = view.findViewById(R.id.member_button);
        managedTeamsButton.setOnClickListener(this::onToggleButtonClick);
        memberTeamsButton.setOnClickListener(this::onToggleButtonClick);
        managedTeamsButton.performClick();
    }

    private void onToggleButtonClick(View view) {
        ToggleButton button = (ToggleButton) view;
        ToggleButton otherButton;

        TeamResponse teamResponse = JsonUtils.parse(RequestUtils.getTeamsRequest(userId), TeamResponse.class);

        if (button.getId() == managedTeamsButton.getId()) {
            fillCarousels(teamResponse.getManagedTeams());
            otherButton = memberTeamsButton;
        } else {
            fillCarousels(teamResponse.getOthersTeams());
            otherButton = managedTeamsButton;
        }

        button.setTextAppearance(R.style.active_teams);
        button.setChecked(true);
        otherButton.setTextAppearance(R.style.default_teams);
        otherButton.setChecked(false);
    }
}