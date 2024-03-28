package com.ilyap.yuta.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.adapters.viewholders.ProjectMemberViewHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.view.View.VISIBLE;

public class ProjectTeamPreviewAdapter extends BaseAdapter<User, BaseAdapter.ViewHolder<User>> {
    private static final int MAX_MEMBERS_COUNT = 5;
    private static final int MORE_USERS = 1;
    private static final int ENOUGH_USERS = 0;
    private final View projectView;

    public ProjectTeamPreviewAdapter(Context context, List<User> items, View projectView) {
        super(context, items);
        this.projectView = projectView;
    }

    @Override
    public @NotNull ViewHolder<User> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_member, parent, false);

        ViewHolder<User> viewHolder;
        switch (viewType) {
            case ENOUGH_USERS:
                viewHolder = new ProjectMemberViewHolder(view, getContext(), getItems().get(0).getId());
                break;
            case MORE_USERS:
            default:
                viewHolder = new ProjectMoreMembersViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= MAX_MEMBERS_COUNT) {
            return MORE_USERS;
        } else {
            return ENOUGH_USERS;
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(getItems().size(), MAX_MEMBERS_COUNT);
    }

    public class ProjectMoreMembersViewHolder extends ViewHolder<User> {
        private final TextView remainingMembersCount;
        private final View member;
        private final View teamContainer;
        private final View closeButton;
        private final RecyclerView teamRecyclerView;
        private boolean isOpen;

        public ProjectMoreMembersViewHolder(@NonNull View itemView) {
            super(itemView);
            this.remainingMembersCount = itemView.findViewById(R.id.plus_members);
            this.member = itemView.findViewById(R.id.member);

            this.teamContainer = projectView.findViewById(R.id.team_container);
            this.closeButton = projectView.findViewById(R.id.close);
            this.teamRecyclerView = projectView.findViewById(R.id.team_list);
            this.isOpen = false;
        }

        @Override
        public void bind(User user) {
            remainingMembersCount.setVisibility(VISIBLE);
            String text = "+" + (getItems().size() - MAX_MEMBERS_COUNT - 1);
            remainingMembersCount.setText(text);

            member.setOnClickListener(v -> {
                setupFullTeamView(getItems());
                slideUp(teamContainer);
            });

            closeButton.setOnClickListener(v -> slideDown(teamContainer));
        }

        private void setupFullTeamView(List<User> team) {
            teamRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), MAX_MEMBERS_COUNT));
            teamRecyclerView.setAdapter(new ProjectFullTeamAdapter(getContext(), team));
        }

        public void slideUp(View view) {
            view.setVisibility(VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight(), 0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);
            isOpen = !isOpen;
        }

        public void slideDown(View view) {
            TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);
        }
    }
}