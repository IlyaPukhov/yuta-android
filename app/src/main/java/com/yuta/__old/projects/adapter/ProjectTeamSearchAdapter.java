package com.yuta.__old.projects.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.yuta.__old.R;
import com.yuta.app.domain.model.entity.Team;
import com.yuta.common.ui.BaseAdapter;
import com.yuta.common.ui.AppDialog;
import com.yuta.__old.projects.dialog.CreateProjectDialog;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProjectTeamSearchAdapter extends BaseAdapter<Team, BaseAdapter.ViewHolder<Team>> {
    private final AppDialog dialog;
    private final ProjectTeamSearchAdapter addedTeamSearchAdapter;

    public ProjectTeamSearchAdapter(AppDialog dialog, List<Team> items, ProjectTeamSearchAdapter teamSearchAdapter) {
        super(dialog.getContext(), items);
        this.dialog = dialog;
        this.addedTeamSearchAdapter = teamSearchAdapter;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_search_in_project, parent, false);
        return new TeamViewHolder(view);
    }

    public class TeamViewHolder extends BaseAdapter.ViewHolder<Team> {
        private final TextView name;
        private final Button buttonAdd;
        private final Button buttonRemove;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.buttonAdd = itemView.findViewById(R.id.add_team);
            this.buttonRemove = itemView.findViewById(R.id.remove_team);
        }

        @Override
        public void bind(Team team) {
            name.setText(team.getName());

            if (addedTeamSearchAdapter != null) {
                buttonRemove.setVisibility(GONE);
                buttonAdd.setVisibility(VISIBLE);

                buttonAdd.setOnClickListener(v -> {
                    List<Team> addedItems = addedTeamSearchAdapter.getItems();
                    if (!addedItems.isEmpty()) {
                        addedTeamSearchAdapter.removeItem(addedItems.get(0));
                    }

                    removeItem(team);
                    addedTeamSearchAdapter.insertItem(team);
                    ((CreateProjectDialog) dialog).updateSubmitButtonState();
                    ((CreateProjectDialog) dialog).updateAddedTextVisibility();
                });
            } else {
                buttonAdd.setVisibility(GONE);
                buttonRemove.setVisibility(VISIBLE);

                buttonRemove.setOnClickListener(v -> {
                    removeItem(team);
                    ((CreateProjectDialog) dialog).updateSubmitButtonState();
                    ((CreateProjectDialog) dialog).updateAddedTextVisibility();
                });
            }
        }
    }
}