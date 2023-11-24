package com.ilyap.yuta.ui.fragments;

import static com.ilyap.yuta.utils.UserUtils.getUserId;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.models.TeamResponse;
import com.ilyap.yuta.utils.JsonUtils;
import com.ilyap.yuta.utils.RequestUtils;

import java.util.List;

public class TeamsFragment extends Fragment {
    ToggleButton managedTeamsButton;
    ToggleButton memberTeamsButton;

    private View view;

    public TeamsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teams, container, false);

        int userId = getUserId(requireActivity());
        TeamResponse teamResponse = JsonUtils.parse(RequestUtils.getTeamsRequest(userId), TeamResponse.class);
        List<Team> managedTeams = teamResponse.getManagedTeams();
        List<Team> othersTeams = teamResponse.getOthersTeams();

        managedTeamsButton = view.findViewById(R.id.manager_button);
        memberTeamsButton = view.findViewById(R.id.member_button);
        managedTeamsButton.setOnClickListener(this::onToggleButtonClick);
        memberTeamsButton.setOnClickListener(this::onToggleButtonClick);

        return view;
    }

    private void onToggleButtonClick(View view) {
        ToggleButton button = (ToggleButton) view;

        ToggleButton otherButton = (button.getId() == managedTeamsButton.getId())
                ? memberTeamsButton
                : managedTeamsButton;

        button.setTextAppearance(R.style.active_teams);
        button.setChecked(true);
        otherButton.setTextAppearance(R.style.default_teams);
        otherButton.setChecked(false);
    }
}