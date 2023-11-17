package com.ilyap.yuta.ui;

import static android.content.Intent.*;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.utils.JsonUtils;
import com.ilyap.yuta.utils.RequestUtils;

public class ProfileFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private View view;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferences = requireActivity().getSharedPreferences("session", Context.MODE_PRIVATE);

        User user = getUser(getAuthorizedUserId());
        fillViews(user);

        view.findViewById(R.id.log_out).setOnClickListener(v -> logOut());
        view.findViewById(R.id.reload).setOnClickListener(v -> reload());
        view.findViewById(R.id.edit).setOnClickListener(v -> edit());
        view.findViewById(R.id.photo).setOnClickListener(v -> openPhotoDialog());

        return view;
    }

    private void fillViews(User user) {
        String fullName = user.getLastName() + " " + user.getFirstName() + (user.getPatronymic() == null ? "" : " " + user.getPatronymic());
        String faculty = getString(R.string.faculty) + ": " + user.getFaculty();
        String direction = getString(R.string.direction) + ": " + user.getDirection();
        String group = getString(R.string.group) + ": " + user.getGroup();
        String projectsCount = user.getDoneProjectsCount() + "/" + user.getAllProjectsCount();
        String tasksCount = user.getDoneTasksCount() + "/" + user.getAllTasksCount();

        Glide.with(this).load(user.getCroppedPhoto()).into((ImageView) view.findViewById(R.id.photo));
        ((TextView) view.findViewById(R.id.name)).setText(fullName);
        ((TextView) view.findViewById(R.id.age)).setText(user.getAge());
        ((TextView) view.findViewById(R.id.faculty)).setText(faculty);
        ((TextView) view.findViewById(R.id.direction)).setText(direction);
        ((TextView) view.findViewById(R.id.group)).setText(group);
        ((TextView) view.findViewById(R.id.projects_count)).setText(projectsCount);
        ((TextView) view.findViewById(R.id.tasks_count)).setText(tasksCount);
        ((TextView) view.findViewById(R.id.teams_count)).setText(String.valueOf(user.getTeamsCount()));

        if (user.getBiography() != null) {
            View bio = view.findViewById(R.id.biography);
            bio.setVisibility(VISIBLE);
            ((TextView) bio).setText(user.getBiography());
        }
        if (user.getPhoneNumber() != null) {
            View phone = view.findViewById(R.id.phone_number);
            phone.setVisibility(VISIBLE);
            ((TextView) phone).setText(user.getPhoneNumber());
        }
        if (user.geteMail() != null) {
            View email = view.findViewById(R.id.email);
            email.setVisibility(VISIBLE);
            ((TextView) email).setText(user.geteMail());
        }
        if (user.getVk() != null) {
            View vk = view.findViewById(R.id.vk);
            vk.setVisibility(VISIBLE);
            ((TextView) vk).setText(user.getVk());
        }
    }

    private User getUser(int id) {
        String json = RequestUtils.getUserRequest(id);
        return JsonUtils.parse(json, User.class);
    }

    private int getAuthorizedUserId() {
        return sharedPreferences.getInt("user_id", -1);
    }

    private void logOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_id").apply();
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void reload() {
        // TODO
    }

    private void edit() {
        // TODO
    }

    private void openPhotoDialog() {
        // TODO
    }
}