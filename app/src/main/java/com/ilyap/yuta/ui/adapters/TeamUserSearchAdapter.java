package com.ilyap.yuta.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.ilyap.yuta.R;
import com.ilyap.yutarefactor.domain.entity.UserUpdateDto;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.team.CreateTeamDialog;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.loadImageToImageViewWithoutCaching;

public class TeamUserSearchAdapter extends BaseAdapter<UserUpdateDto, BaseAdapter.ViewHolder<UserUpdateDto>> {
    private final CustomDialog dialog;
    private final TeamUserSearchAdapter membersAdapter;

    public TeamUserSearchAdapter(CustomDialog dialog, List<UserUpdateDto> items, TeamUserSearchAdapter membersAdapter) {
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

    public class UserViewHolder extends BaseAdapter.ViewHolder<UserUpdateDto> {
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
        public void bind(UserUpdateDto userDto) {
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