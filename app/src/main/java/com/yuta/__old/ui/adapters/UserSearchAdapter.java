package com.yuta.__old.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import com.yuta.common.util.GlideUtils;
import com.yuta.app.MainActivity;
import com.yuta.__old.R;
import com.yuta.app.domain.model.entity.User;
import com.yuta.app.viewmodel.MainViewModel;

import java.util.List;

import static com.yuta.common.util.UserUtils.getUserId;

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
        public void bind(User userDto) {
            GlideUtils.loadImageToImageViewWithCaching(avatar, userDto.getCroppedPhoto());

            String fullName = userDto.getLastName() + " " + userDto.getFirstName() + (userDto.getPatronymic() == null ? "" : " " + userDto.getPatronymic());
            name.setText(fullName);

            userLayout.setOnClickListener(v -> {
                hideKeyboard();

                MainActivity activity = (MainActivity) getContext();
                if (getUserId(getContext()) == userDto.getId()) {
                    activity.selectNavTab(R.id.profileFragment);
                } else {
                    MainViewModel mainViewModel = new ViewModelProvider(activity).get(MainViewModel.class);
                    NavController navController = activity.getNavController();
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", userDto.getId());
                    navController.navigate(R.id.action_searchFragment_to_readonlyProfileFragment, bundle);
                    mainViewModel.setReadonlyProfile(true);
                }
            });
        }
    }
}