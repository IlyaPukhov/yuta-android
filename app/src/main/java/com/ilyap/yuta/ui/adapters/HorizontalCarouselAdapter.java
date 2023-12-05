package com.ilyap.yuta.ui.adapters;

import android.content.Context;
import android.util.AttributeSet;
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

    private static class SpanningLinearLayoutManager extends LinearLayoutManager {

        public SpanningLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
            return spanLayoutSize(super.generateDefaultLayoutParams());
        }

        @Override
        public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
            return spanLayoutSize(super.generateLayoutParams(c, attrs));
        }

        @Override
        public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
            return spanLayoutSize(super.generateLayoutParams(lp));
        }

        @Override
        public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
            return super.checkLayoutParams(lp);
        }

        private RecyclerView.LayoutParams spanLayoutSize(RecyclerView.LayoutParams layoutParams) {
            if (getOrientation() == HORIZONTAL) {
                layoutParams.width = (int) Math.round(getHorizontalSpace() / (double) getItemCount());
            } else if (getOrientation() == VERTICAL) {
                layoutParams.height = (int) Math.round(getVerticalSpace() / (double) getItemCount());
            }
            return layoutParams;
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }

        @Override
        public boolean canScrollHorizontally() {
            return false;
        }

        private int getHorizontalSpace() {
            return getWidth() - getPaddingRight() - getPaddingLeft();
        }

        private int getVerticalSpace() {
            return getHeight() - getPaddingBottom() - getPaddingTop();
        }
    }
}