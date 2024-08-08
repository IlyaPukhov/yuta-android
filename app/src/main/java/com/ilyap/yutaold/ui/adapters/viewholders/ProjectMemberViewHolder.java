package com.ilyap.yutaold.ui.adapters.viewholders;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import com.ilyap.yutaold.R;
import com.ilyap.yutaold.ui.adapters.BaseAdapter;
import com.ilyap.yuta.MainActivity;
import com.ilyap.yuta.domain.model.entity.User;
import lombok.Getter;

import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.ImageUtils.loadImageToImageViewWithoutCaching;
import static com.ilyap.yuta.utils.UserUtils.getUserId;

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
    public void bind(User userDto) {
        imageView.setVisibility(VISIBLE);
        loadImageToImageViewWithoutCaching(imageView, userDto.getCroppedPhoto());

        if (managerId == userDto.getId()) {
            teamLeaderIcon.setVisibility(VISIBLE);
        }

        String userName = userDto.getLastName() + " " + userDto.getFirstName();
        name.setText(userName);

        member.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getContext();
            if (getUserId(getContext()) == userDto.getId()) {
                activity.selectNavTab(R.id.profileFragment);
            } else {
                NavController navController = activity.getNavController();
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userDto.getId());
                navController.navigate(R.id.action_projectsFragment_to_readonlyProfileFragment, bundle);
                activity.setReadonlyProfile(true);
            }
        });
    }
}