package com.ilyap.yutaold.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.ilyap.yutaold.R;
import com.ilyap.yuta.domain.model.entity.User;
import com.ilyap.yutaold.ui.adapters.viewholders.ProjectMemberViewHolder;

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