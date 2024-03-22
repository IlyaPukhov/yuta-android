package com.ilyap.yuta.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import com.ilyap.yuta.MainActivity;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;
import static com.ilyap.yuta.utils.UserUtils.loadImage;

public class ProjectTeamPreviewAdapter extends BaseAdapter<User, BaseAdapter.ViewHolder<User>> {
    private static final int MAX_MEMBERS_COUNT = 4;
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
                viewHolder = new UserViewHolder(view);
                break;
            case MORE_USERS:
            default:
                viewHolder = new MoreUsersViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position > 4) {
            return MORE_USERS;
        } else {
            return ENOUGH_USERS;
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(getItems().size(), MAX_MEMBERS_COUNT + 1);
    }

    public class UserViewHolder extends ViewHolder<User> {
        private final ImageView imageView;
        private final TextView name;
        private final View member;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.imageView = itemView.findViewById(R.id.avatar);
            this.member = itemView.findViewById(R.id.member);
        }

        @Override
        public void bind(User user) {
            imageView.setVisibility(VISIBLE);
            loadImage(getContext(), user.getCroppedPhotoUrl(), imageView);

            String userName = user.getLastName() + " " + user.getFirstName();
            name.setText(userName);

            member.setOnClickListener(v -> {
                NavController navController = ((MainActivity) getContext()).getNavController();
                if (getUserId(getContext()) == user.getId()) {
                    navController.navigate(R.id.action_projectsFragment_to_profileFragment);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", user.getId());
                    navController.navigate(R.id.action_projectsFragment_to_readonlyProfileFragment, bundle);
                }
            });
        }
    }

    public class MoreUsersViewHolder extends ViewHolder<User> {
        private final TextView remainingMembersCount;
        private final View member;
        private final View teamContainer;
        private final View closeButton;
        private final View teamRecyclerView;
        private boolean isOpen;

        public MoreUsersViewHolder(@NonNull View itemView) {
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
            String text = "+" + (getItems().size() - MAX_MEMBERS_COUNT);
            remainingMembersCount.setText(text);

            member.setOnClickListener(v -> {
                setupFullTeamView(getItems());
                slideUp(teamContainer);
            });

            closeButton.setOnClickListener(v -> slideDown(teamContainer));
        }

        private void setupFullTeamView(List<User> team) {
            // TODO: 23.03.2024 team view 


        }


        public void slideUp(View view) {
            view.setVisibility(View.VISIBLE);
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