package com.ilyap.yuta.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.ilyap.yuta.R;
import com.ilyap.yutarefactor.domain.entity.UserUpdateDto;
import com.ilyap.yuta.ui.adapters.viewholders.ProjectMemberViewHolder;

import java.util.List;

public class ProjectFullTeamAdapter extends BaseAdapter<UserUpdateDto, BaseAdapter.ViewHolder<UserUpdateDto>> {

    public ProjectFullTeamAdapter(Context context, List<UserUpdateDto> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public ViewHolder<UserUpdateDto> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_member, parent, false);
        return new ProjectMemberViewHolder(view, getContext(), getItems().get(0).getId());
    }
}