package com.ilyap.yuta.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import com.ilyap.yuta.MainActivity;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.fragments.ProfileFragment;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;
import static com.ilyap.yuta.utils.UserUtils.loadImageToImageView;

public class SearchAdapter extends BaseAdapter<User, BaseAdapter.ViewHolder<User>> {

    public SearchAdapter(Context context, List<User> items) {
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
            loadImageToImageView(avatar, user.getCroppedPhotoUrl());

            String fullName = user.getLastName() + " " + user.getFirstName() + (user.getPatronymic() == null ? "" : " " + user.getPatronymic());
            name.setText(fullName);

            userLayout.setOnClickListener(v -> {
                hideKeyboard();

                NavController navController = ((MainActivity) getContext()).getNavController();
                if (getUserId(getContext()) == user.getId()) {
//                    navController.navigate(R.id.action_searchFragment_to_profileFragment);
                    // TODO: 30.03.2024  
                    ((MainActivity) getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, new ProfileFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", user.getId());
                    navController.navigate(R.id.action_searchFragment_to_readonlyProfileFragment, bundle);
                }
            });
        }

        private void hideKeyboard() {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
            View view = new View(getContext());
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
}