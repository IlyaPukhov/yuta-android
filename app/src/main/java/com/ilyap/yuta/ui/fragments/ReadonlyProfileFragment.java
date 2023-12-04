package com.ilyap.yuta.ui.fragments;

import static android.view.View.GONE;
import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.utils.RequestViewModel;

public class ReadonlyProfileFragment extends ProfileFragment {

    public ReadonlyProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        progressLayout = view.findViewById(R.id.progressLayout);

        viewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        updateProfile();

        view.findViewById(R.id.log_out).setVisibility(GONE);
        view.findViewById(R.id.reload).setVisibility(GONE);
        view.findViewById(R.id.edit).setVisibility(GONE);

        return view;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStack("profileFragmentTransaction", POP_BACK_STACK_INCLUSIVE);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}