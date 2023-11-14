package com.ilyap.yuta.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.utils.JsonUtils;
import com.ilyap.yuta.utils.RequestUtils;

public class ProfileFragment extends Fragment {
    private SharedPreferences sharedPreferences;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferences = requireActivity().getSharedPreferences("session", Context.MODE_PRIVATE);

        User user = getUser(getAuthorizedUserId());
        fillViews(user);

        // TODO обработчики кнопок

        return view;
    }

    private void fillViews(User user) {
        // TODO заполнение полей
    }

    private User getUser(int id) {
        String json = RequestUtils.getUserRequest(id);
        return JsonUtils.parse(json, User.class);
    }

    private int getAuthorizedUserId() {
        return sharedPreferences.getInt("user_id", -1);
    }
}