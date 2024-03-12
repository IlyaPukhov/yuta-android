package com.ilyap.yuta.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.project.CreateProjectDialog;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TeamAdapter extends BaseAdapter<Team, BaseAdapter.ViewHolder<Team>> {
    private final CustomDialog dialog;
    private final TeamAdapter addedTeamAdapter;

    public TeamAdapter(CustomDialog dialog, List<Team> items, TeamAdapter teamAdapter) {
        super(dialog.getContext(), items);
        this.dialog = dialog;
        this.addedTeamAdapter = teamAdapter;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_search, parent, false);
        return new TeamViewHolder(view);
    }

    public class TeamViewHolder extends ViewHolder<Team> {
        private final TextView name;
        private final Button buttonAdd;
        private final Button buttonRemove;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.buttonAdd = itemView.findViewById(R.id.btnAdd);
            this.buttonRemove = itemView.findViewById(R.id.btnRemove);
        }

        @Override
        public void bind(Team team) {
            name.setText(team.getName());

            if (addedTeamAdapter != null) {
                buttonRemove.setVisibility(GONE);
                buttonAdd.setVisibility(VISIBLE);

                buttonAdd.setOnClickListener(v -> {
                    removeItem(team);
                    addedTeamAdapter.getItems().clear();
                    addedTeamAdapter.insertItem(team);
                    ((CreateProjectDialog) dialog).updateAddedTextVisibility();
                });
            } else {
                buttonAdd.setVisibility(GONE);
                buttonRemove.setVisibility(VISIBLE);

                buttonRemove.setOnClickListener(v -> {
                    removeItem(team);
                    ((CreateProjectDialog) dialog).updateAddedTextVisibility();
                });
            }
        }
    }
}