package com.yuta.teams.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yuta.app.R
import com.yuta.app.domain.model.entity.TeamMember;
import com.yuta.common.ui.SpanningLinearLayoutManager;
import com.yuta.common.ui.BaseAdapter;

import java.util.List;

public class HorizontalCarouselAdapter extends BaseAdapter<List<TeamMember>, BaseAdapter.ViewHolder<List<TeamMember>>> {

    public HorizontalCarouselAdapter(Context context, List<List<TeamMember>> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_carousel, parent, false);
        view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new HorizontalViewHolder(view);
    }

    public class HorizontalViewHolder extends BaseAdapter.ViewHolder<List<TeamMember>> {
        private final RecyclerView horizontalRecyclerView;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontalRecyclerView);
        }

        @Override
        public void bind(List<TeamMember> members) {
            horizontalRecyclerView.setLayoutManager(new SpanningLinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            horizontalRecyclerView.setAdapter(new TeamMembersAdapter(getContext(), members));
        }
    }
}