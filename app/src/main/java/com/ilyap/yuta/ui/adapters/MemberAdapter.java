package com.ilyap.yuta.ui.adapters;

import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.loadImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.TeamMember;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.fragments.ReadonlyProfileFragment;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private final List<TeamMember> members;
    private final Context context;

    public MemberAdapter(List<TeamMember> members, Context context) {
        this.members = members;
        this.context = context;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel_card, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.bind(members.get(position));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        private final View card;
        private final ImageView imageView;
        private final TextView name;
        private final ImageView teamLeaderIcon;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            teamLeaderIcon = itemView.findViewById(R.id.teamLeaderIcon);
        }

        public void bind(TeamMember member) {
            User user = member.getMember();
            loadImage(context, user.getCroppedPhoto(), imageView);

            if (member.getTeam().getLeader().equals(user)) {
                teamLeaderIcon.setVisibility(VISIBLE);
            }

            String userName = user.getLastName() + " " + user.getFirstName();
            name.setText(userName);

            card.setOnClickListener(v -> openProfile(user));
        }

        private void openProfile(User user) {
            ReadonlyProfileFragment profileFragment = new ReadonlyProfileFragment();
            profileFragment.setUser(user);

            AppCompatActivity activity = (AppCompatActivity) context;
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.teamsContainer, profileFragment)
                    .addToBackStack("profileFragmentTransaction")
                    .commit();
        }
    }
}