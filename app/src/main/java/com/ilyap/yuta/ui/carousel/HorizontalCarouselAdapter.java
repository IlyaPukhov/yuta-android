package com.ilyap.yuta.ui.carousel;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilyap.yuta.R;

import java.util.List;

public class HorizontalCarouselAdapter extends RecyclerView.Adapter<HorizontalCarouselAdapter.HorizontalViewHolder> {
    private final List<List<Integer>> pages;

    public HorizontalCarouselAdapter(List<List<Integer>> pages) {
        this.pages = pages;
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
        List<Integer> imageList = pages.get(position);
        holder.bind(imageList);
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    public static class HorizontalViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView horizontalRecyclerView;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontalRecyclerView);
        }

        public void bind(List<Integer> imageList) {
            horizontalRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(10));

            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            horizontalRecyclerView.setLayoutManager(layoutManager);

            ImageAdapter imageAdapter = new ImageAdapter(imageList);
            horizontalRecyclerView.setAdapter(imageAdapter);
        }
    }

    public static class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int horizontalSpaceWidth;

        public HorizontalSpaceItemDecoration(int horizontalSpaceWidth) {
            this.horizontalSpaceWidth = horizontalSpaceWidth;
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.right = horizontalSpaceWidth;
            }
        }
    }
}