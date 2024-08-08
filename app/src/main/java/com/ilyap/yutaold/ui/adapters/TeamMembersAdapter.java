package com.ilyap.yutaold.ui.adapters;

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
import com.ilyap.yutaold.R;
import com.ilyap.yuta.domain.entity.TeamMember;
import com.ilyap.yuta.domain.entity.UserUpdateDto;

import java.util.List;

import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.ImageUtils.loadImageToImageViewWithoutCaching;
import static com.ilyap.yuta.utils.UserUtils.getUserId;

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
            UserUpdateDto userDto = member.getMember();
            UserUpdateDto leader = member.getTeam().getLeader();
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