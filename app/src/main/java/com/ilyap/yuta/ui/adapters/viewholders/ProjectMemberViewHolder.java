package com.ilyap.yuta.ui.adapters.viewholders;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import com.ilyap.yuta.MainActivity;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.adapters.BaseAdapter;
import lombok.Getter;

import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;
import static com.ilyap.yuta.utils.UserUtils.loadImageToImageView;

public class ProjectMemberViewHolder extends BaseAdapter.ViewHolder<User> {
    @Getter
    private final Context context;
    private final ImageView imageView;
    private final TextView name;
    private final View member;
    private final ImageView teamLeaderIcon;
    private final int managerId;

    public ProjectMemberViewHolder(@NonNull View itemView, Context context, int managerId) {
        super(itemView);
        this.context = context;
        this.name = itemView.findViewById(R.id.name);
        this.imageView = itemView.findViewById(R.id.avatar);
        this.member = itemView.findViewById(R.id.member);
        this.teamLeaderIcon = itemView.findViewById(R.id.teamLeaderIcon);

        this.managerId = managerId;
    }

    @Override
    public void bind(User user) {
        imageView.setVisibility(VISIBLE);
        loadImageToImageView(imageView, user.getCroppedPhoto());

        if (managerId == user.getId()) {
            teamLeaderIcon.setVisibility(VISIBLE);
        }

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