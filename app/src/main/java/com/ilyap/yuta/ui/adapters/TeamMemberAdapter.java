package com.ilyap.yuta.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import com.ilyap.yuta.MainActivity;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.TeamMember;
import com.ilyap.yuta.models.User;

import java.util.List;

import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;
import static com.ilyap.yuta.utils.UserUtils.loadImageToImageView;

public class TeamMemberAdapter extends BaseAdapter<TeamMember, BaseAdapter.ViewHolder<TeamMember>> {

    public TeamMemberAdapter(Context context, List<TeamMember> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public TeamMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_member_card, parent, false);
        return new TeamMemberViewHolder(view);
    }

    public class TeamMemberViewHolder extends BaseAdapter.ViewHolder<TeamMember> {
        private final View card;
        private final ImageView imageView;
        private final TextView name;
        private final ImageView teamLeaderIcon;

        public TeamMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            this.card = itemView.findViewById(R.id.card);
            this.imageView = itemView.findViewById(R.id.avatar);
            this.name = itemView.findViewById(R.id.name);
            this.teamLeaderIcon = itemView.findViewById(R.id.teamLeaderIcon);
        }

        @Override
        public void bind(@NonNull TeamMember member) {
            User user = member.getMember();
            User leader = member.getTeam().getLeader();
            loadImageToImageView(imageView, user.getCroppedPhotoUrl());

            if (leader.equals(user)) {
                teamLeaderIcon.setVisibility(VISIBLE);
            }

            String userName = user.getLastName() + " " + user.getFirstName();
            name.setText(userName);

            card.setOnClickListener(v -> {
                NavController navController = ((MainActivity) getContext()).getNavController();
                if (getUserId(getContext()) == user.getId()) {
                    navController.navigate(R.id.action_teamsFragment_to_profileFragment);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", user.getId());
                    navController.navigate(R.id.action_teamsFragment_to_readonlyProfileFragment, bundle);
                }
            });
        }
    }
}