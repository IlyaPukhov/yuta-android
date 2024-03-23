package com.ilyap.yuta.ui.dialogs.team;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.ui.fragments.TeamsFragment;
import com.ilyap.yuta.utils.RequestViewModel;

@SuppressWarnings("ConstantConditions")
public class DeleteTeamDialog extends CustomInteractiveDialog {
    private final Team team;
    private RequestViewModel viewModel;

    public DeleteTeamDialog(Context context, Fragment fragment, Team team) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_delete);
        this.team = team;
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);
        setupTextView(team.getName());

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> {
            deleteTeam(fragment, team);
            dismiss();
        });
    }

    private void setupTextView(String name) {
        String text = getContext().getString(R.string.delete_team_desc) + " \"" + name + "\"?";
        ((TextView) dialog.findViewById(R.id.name_desc)).setText(text);
    }

    private void deleteTeam(Fragment fragment, @NonNull Team team) {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.deleteTeam(team.getId());
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            ((TeamsFragment) fragment).updateList();
            dismiss();
        });
    }
}