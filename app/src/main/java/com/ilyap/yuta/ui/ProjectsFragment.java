package com.ilyap.yuta.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilyap.yuta.R;

public class ProjectsFragment extends Fragment {

    public ProjectsFragment() {
    }

    public static ProjectsFragment newInstance() {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }
}