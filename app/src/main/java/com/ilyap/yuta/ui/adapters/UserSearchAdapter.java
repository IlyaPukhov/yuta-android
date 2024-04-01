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
import com.ilyap.yuta.models.User;

import java.util.List;

import static com.ilyap.yuta.utils.UserUtils.getUserId;
import static com.ilyap.yuta.utils.UserUtils.loadImageToImageView;

public class UserSearchAdapter extends BaseAdapter<User, BaseAdapter.ViewHolder<User>> {

    public UserSearchAdapter(Context context, List<User> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public UserSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search, parent, false);
        return new UserSearchViewHolder(view);
    }

    public class UserSearchViewHolder extends ViewHolder<User> {
        private final TextView name;
        private final ImageView avatar;
        private final View userLayout;

        public UserSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.avatar = itemView.findViewById(R.id.avatar);
            this.userLayout = itemView.findViewById(R.id.user_layout);
        }

        @Override
        public void bind(User user) {
            loadImageToImageView(avatar, user.getCroppedPhoto());

            String fullName = user.getLastName() + " " + user.getFirstName() + (user.getPatronymic() == null ? "" : " " + user.getPatronymic());
            name.setText(fullName);

            userLayout.setOnClickListener(v -> {
                hideKeyboard();

                if (getUserId(getContext()) == user.getId()) {
                    ((MainActivity) getContext()).selectNavTab(R.id.profileFragment);
                } else {
                    NavController navController = ((MainActivity) getContext()).getNavController();
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", user.getId());
                    navController.navigate(R.id.action_searchFragment_to_readonlyProfileFragment, bundle);
                }
            });
        }
    }
}