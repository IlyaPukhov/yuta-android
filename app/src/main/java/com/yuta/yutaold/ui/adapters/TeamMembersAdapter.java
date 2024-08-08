package com.yuta.yutaold.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import com.yuta.app.MainActivity;
import com.yuta.yutaold.R;
import com.yuta.app.domain.model.entity.TeamMember;
import com.yuta.app.domain.model.entity.User;

import java.util.List;

import static android.view.View.VISIBLE;
import static com.yuta.app.utils.ImageUtils.loadImageToImageViewWithoutCaching;
import static com.yuta.app.utils.UserUtils.getUserId;

public class TeamMembersAdapter extends BaseAdapter<TeamMember, BaseAdapter.ViewHolder<TeamMember>> {

    public TeamMembersAdapter(Context context, List<TeamMember> items) {
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
            User userDto = member.getMember();
            User leader = member.getTeam().getLeader();
            loadImageToImageViewWithoutCaching(imageView, userDto.getCroppedPhoto());

            if (leader.equals(userDto)) {
                teamLeaderIcon.setVisibility(VISIBLE);
            }

            String userName = userDto.getLastName() + " " + userDto.getFirstName();
            name.setText(userName);

            card.setOnClickListener(v -> {
                MainActivity activity = (MainActivity) getContext();
                if (getUserId(getContext()) == userDto.getId()) {
                    activity.selectNavTab(R.id.profileFragment);
                } else {
                    NavController navController = activity.getNavController();
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", userDto.getId());
                    navController.navigate(R.id.action_teamsFragment_to_readonlyProfileFragment, bundle);
                    activity.setReadonlyProfile(true);
                }
            });
        }
    }
}