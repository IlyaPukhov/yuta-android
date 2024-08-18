package com.yuta.projects.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.yuta.__old.R;
import com.yuta.app.domain.model.entity.User;
import com.yuta.common.ui.BaseAdapter;

import java.util.List;

public class ProjectFullTeamAdapter extends BaseAdapter<User, BaseAdapter.ViewHolder<User>> {

    public ProjectFullTeamAdapter(Context context, List<User> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public ViewHolder<User> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_member, parent, false);
        return new ProjectMemberViewHolder(view, getContext(), getItems().get(0).getId());
    }
}