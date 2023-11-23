package com.ilyap.yuta.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ilyap.yuta.R;

public class TeamsFragment extends Fragment {

    private View view;

    public TeamsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        //user = getUser(getUserId(requireActivity()));

        return view;
    }


}