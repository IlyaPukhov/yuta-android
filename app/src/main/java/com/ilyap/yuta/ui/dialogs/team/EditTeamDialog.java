package com.ilyap.yuta.ui.dialogs.team;

import android.content.Context;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.ilyap.yuta.R;
import com.ilyap.yutarefactor.domain.entity.Team;
import com.ilyap.yutarefactor.domain.response.TeamResponse;
import com.ilyap.yutarefactor.domain.response.UpdateResponse;
import com.ilyap.yuta.ui.fragments.TeamsFragment;
import com.ilyap.yuta.utils.RequestViewModel;

@SuppressWarnings("ConstantConditions")
public class EditTeamDialog extends CreateTeamDialog {
    private final int teamId;
    private Team team;

    public EditTeamDialog(Context context, Fragment fragment, int teamId) {
        super(context, fragment);
        this.teamId = teamId;
    }

    @Override
    public void start() {
        super.start();
        setupViews();
        getTeam(teamId);

        submitButton.setEnabled(false);
        submitButton.setOnClickListener(v -> {
            hideKeyboard(submitButton);
            editTeam();
        });
    }

    private void setupViews() {
        submitButton.setText(R.string.save_button);
        ((TextView) dialog.findViewById(R.id.create_text)).setText(getContext().getString(R.string.edit_team_text));
    }

    private void getTeam(int teamId) {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.getTeam(teamId);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof TeamResponse)) return;
            team = ((TeamResponse) result).getTeam();
            setupTeam();
        });
    }

    private void setupTeam() {
        teamName.setText(team.getName());
        membersAdapter.updateList(team.getMembers());
        updateAddedTextVisibility();
    }

    private void editTeam() {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.editTeam(teamId, getData(teamName), addedMembers);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            ((TeamsFragment) fragment).updateLists();
            dismiss();
        });
    }

    @Override
    protected RequestViewModel isNameUnique(String name) {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.checkUniqueTeamName(name, teamId);
        return viewModel;
    }
}