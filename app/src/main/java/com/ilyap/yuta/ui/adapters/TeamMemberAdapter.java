package com.ilyap.yuta.ui.adapters;

import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.loadImage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.TeamMember;
import com.ilyap.yuta.models.User;

import java.util.List;

public class TeamMemberAdapter extends BaseAdapter<TeamMember, BaseAdapter.ViewHolder<TeamMember>> {

    public TeamMemberAdapter(Context context, List<TeamMember> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public TeamMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel_card, parent, false);
        return new TeamMemberViewHolder(view);
    }

    public class TeamMemberViewHolder extends BaseAdapter.ViewHolder<TeamMember> {
        private final View card;
        private final ImageView imageView;
        private final TextView name;
        private final ImageView teamLeaderIcon;

        public TeamMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            teamLeaderIcon = itemView.findViewById(R.id.teamLeaderIcon);
        }

        @Override
        public void bind(TeamMember member) {
            User user = member.getMember();
            loadImage(getContext(), user.getCroppedPhoto(), imageView);

            if (member.getTeam().getLeader().equals(user)) {
                teamLeaderIcon.setVisibility(VISIBLE);
            }

            String userName = user.getLastName() + " " + user.getFirstName();
            name.setText(userName);

            card.setOnClickListener(v -> openProfile(user));
        }

        private void openProfile(User user) {
            Bundle bundle = new Bundle();
            bundle.putInt("userId", user.getId());

            Navigation.findNavController((Activity) getContext(), R.id.nav_host_fragment)
                    .navigate(R.id.readonlyProfileFragment, bundle);
        }
    }
}