package com.yuta.__old.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.yuta.__old.R;
import com.yuta.app.domain.model.entity.User;
import com.yuta.common.ui.BaseAdapter;
import com.yuta.common.ui.AppDialog;
import com.yuta.__old.ui.dialog.team.CreateTeamDialog;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.yuta.common.util.GlideUtils.loadImageToImageViewWithoutCaching;

public class TeamUserSearchAdapter extends BaseAdapter<User, BaseAdapter.ViewHolder<User>> {
    private final AppDialog dialog;
    private final TeamUserSearchAdapter membersAdapter;

    public TeamUserSearchAdapter(AppDialog dialog, List<User> items, TeamUserSearchAdapter membersAdapter) {
        super(dialog.getContext(), items);
        this.membersAdapter = membersAdapter;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search_in_team, parent, false);
        return new UserViewHolder(view);
    }

    public class UserViewHolder extends BaseAdapter.ViewHolder<User> {
        private final TextView name;
        private final ImageView avatar;
        private final Button buttonAdd;
        private final Button buttonRemove;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.avatar = itemView.findViewById(R.id.avatar);
            this.buttonAdd = itemView.findViewById(R.id.btnAdd);
            this.buttonRemove = itemView.findViewById(R.id.btnRemove);
        }

        @Override
        public void bind(User userDto) {
            loadImageToImageViewWithoutCaching(avatar, userDto.getCroppedPhoto());

            String userName = userDto.getLastName() + " " + userDto.getFirstName() + (userDto.getPatronymic() == null ? "" : " " + userDto.getPatronymic());
            name.setText(userName);

            if (membersAdapter != null) {
                buttonRemove.setVisibility(GONE);
                buttonAdd.setVisibility(VISIBLE);

                buttonAdd.setOnClickListener(v -> {
                    removeItem(userDto);
                    membersAdapter.insertItem(userDto);
                    ((CreateTeamDialog) dialog).updateAddedTextVisibility();
                });
            } else {
                buttonAdd.setVisibility(GONE);
                buttonRemove.setVisibility(VISIBLE);

                buttonRemove.setOnClickListener(v -> {
                    removeItem(userDto);
                    ((CreateTeamDialog) dialog).updateAddedTextVisibility();
                });
            }
        }
    }
}