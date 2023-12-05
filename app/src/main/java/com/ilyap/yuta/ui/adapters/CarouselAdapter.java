package com.ilyap.yuta.ui.adapters;

import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.models.TeamMember;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.team.DeleteTeamDialog;
import com.ilyap.yuta.ui.dialogs.team.EditTeamDialog;

import java.util.ArrayList;
import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    private static final int PAGE_SIZE = 2;
    private final List<List<TeamMember>> carouselList;
    private final Context context;
    private final Fragment fragment;

    public CarouselAdapter(List<List<TeamMember>> carouselList, Context context, Fragment fragment) {
        this.carouselList = carouselList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        holder.bind(carouselList.get(position));
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
        private final Button btnPrev, btnNext;
        private final Button editTeam, deleteTeam;
        private Team team;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            carouselNumberTextView = itemView.findViewById(R.id.teamName);
            imagePager = itemView.findViewById(R.id.imagePager);
            dotsLayout = itemView.findViewById(R.id.dotsLayout);
            btnPrev = itemView.findViewById(R.id.btnPrev);
            btnNext = itemView.findViewById(R.id.btnNext);
            editTeam = itemView.findViewById(R.id.editTeam);
            deleteTeam = itemView.findViewById(R.id.deleteTeam);
        }

        public void bind(List<TeamMember> carousel) {
            team = carousel.get(0).getTeam();
            carouselNumberTextView.setText(team.getName());
            List<List<TeamMember>> pages = getPagesList(carousel);

            HorizontalCarouselAdapter horizontalCarouselAdapter = new HorizontalCarouselAdapter(pages, context);
            imagePager.setAdapter(horizontalCarouselAdapter);

            setupDots(pages.size());
            setupNavButtons(pages);
            setupDataButtons(team.getLeader().getId());

            imagePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    updateDots(position);
                }
            });
        }

        private void setupDataButtons(int leaderId) {
            if (leaderId == getUserId((Activity) context)) {
                editTeam.setVisibility(VISIBLE);
                deleteTeam.setVisibility(VISIBLE);
                editTeam.setOnClickListener(v -> openEditTeam());
                deleteTeam.setOnClickListener(v -> openDeleteTeam());
            }
        }

        private void openDeleteTeam() {
            CustomDialog deleteTeamDialog = new DeleteTeamDialog(context, fragment, team);
            deleteTeamDialog.start();
        }

        private void openEditTeam() {
            CustomDialog editTeamDialog = new EditTeamDialog(context, fragment, team.getId());
            editTeamDialog.start();
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

        private <T> void setupNavButtons(List<List<T>> pages) {
            if (pages.size() < 2) {
                btnPrev.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_gray));
                btnNext.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_gray));
            }

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

        private <T> List<List<T>> getPagesList(List<T> list) {
            List<List<T>> pages = new ArrayList<>();
            for (int i = 0; i < list.size(); i += PAGE_SIZE) {
                int end = Math.min(list.size(), i + PAGE_SIZE);
                pages.add(new ArrayList<>(list.subList(i, end)));
            }
            return pages;
        }
    }
}