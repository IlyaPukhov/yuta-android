package com.ilyap.yuta.ui.carousel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.ilyap.yuta.R;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    private final List<ImageModel> carouselList;
    private final Context context;

    public CarouselAdapter(List<ImageModel> carouselList, Context context) {
        this.carouselList = carouselList;
        this.context = context;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        ImageModel carousel = carouselList.get(position);
        holder.bind(carousel);
    }

    @Override
    public int getItemCount() {
        return carouselList.size();
    }

    @SuppressWarnings("ConstantConditions")
    public class CarouselViewHolder extends RecyclerView.ViewHolder {
        private final TextView carouselNumberTextView;
        private final ViewPager2 imagePager;
        private final LinearLayout dotsLayout;
        private final ImageButton btnPrev, btnNext;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            carouselNumberTextView = itemView.findViewById(R.id.teamName);
            imagePager = itemView.findViewById(R.id.imagePager);
            dotsLayout = itemView.findViewById(R.id.dotsLayout);
            btnPrev = itemView.findViewById(R.id.btnPrev);
            btnNext = itemView.findViewById(R.id.btnNext);
        }

        public void bind(ImageModel carousel) {
            carouselNumberTextView.setText("Carousel " + carousel.getCarouselNumber());
            List<List<Integer>> pages = carousel.getPagesList();

            HorizontalCarouselAdapter horizontalCarouselAdapter = new HorizontalCarouselAdapter(pages);
            imagePager.setAdapter(horizontalCarouselAdapter);

            setupDots(pages.size());
            setupButtons(pages);

            imagePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    updateDots(position);
                }
            });
        }

        private void setupDots(int size) {
            for (int i = 0; i < size; i++) {
                ImageView dot = new ImageView(context);
                dot.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dot_not_selected));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(8, 0, 8, 0);

                dotsLayout.addView(dot, params);
            }

            updateDots(0);
        }

        private void updateDots(int position) {
            for (int i = 0; i < dotsLayout.getChildCount(); i++) {
                ImageView dot = (ImageView) dotsLayout.getChildAt(i);
                if (i == position) {
                    dot.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dot_selected));
                } else {
                    dot.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dot_not_selected));
                }
            }
        }

        private void setupButtons(List<List<Integer>> pages) {
            btnPrev.setOnClickListener(v -> {
                int currentPos = imagePager.getCurrentItem();
                if (currentPos > 0) {
                    imagePager.setCurrentItem(currentPos - 1);
                } else if (currentPos == 0) {
                    imagePager.setCurrentItem(pages.size() - 1);
                }
            });

            btnNext.setOnClickListener(v -> {
                int currentPos = imagePager.getCurrentItem();
                int lastPos = imagePager.getAdapter().getItemCount() - 1;

                if (currentPos < lastPos) {
                    imagePager.setCurrentItem(currentPos + 1);
                } else if (currentPos == lastPos) {
                    imagePager.setCurrentItem(0);
                }
            });
        }
    }
}