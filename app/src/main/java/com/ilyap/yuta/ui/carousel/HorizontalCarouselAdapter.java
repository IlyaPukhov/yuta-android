package com.ilyap.yuta.ui.carousel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.TeamMember;

import java.util.List;

public class HorizontalCarouselAdapter extends RecyclerView.Adapter<HorizontalCarouselAdapter.HorizontalViewHolder> {
    private final List<List<TeamMember>> pages;
    private final Context context;

    public HorizontalCarouselAdapter(List<List<TeamMember>> pages, Context context) {
        this.pages = pages;
        this.context = context;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_carousel, parent, false);
        view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        holder.bind(pages.get(position));
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView horizontalRecyclerView;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontalRecyclerView);
        }

        public void bind(List<TeamMember> members) {
            LinearLayoutManager layoutManager = new SpanningLinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            horizontalRecyclerView.setLayoutManager(layoutManager);

            MemberAdapter memberAdapter = new MemberAdapter(members, context);
            horizontalRecyclerView.setAdapter(memberAdapter);
        }
    }
}