package com.ilyap.yuta.ui.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.ui.dialogs.photo.UploadPhotoDialog.PICK_IMAGE_REQUEST;
import static com.ilyap.yuta.utils.UserUtils.getUserId;
import static com.ilyap.yuta.utils.UserUtils.loadImage;
import static com.ilyap.yuta.utils.UserUtils.logOut;
import static com.ilyap.yuta.utils.UserUtils.setCurrentUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ilyap.yuta.MainActivity;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.photo.PhotoDialog;
import com.ilyap.yuta.ui.dialogs.photo.UploadPhotoDialog;
import com.ilyap.yuta.ui.dialogs.user.EditUserDialog;
import com.ilyap.yuta.ui.dialogs.user.UpdateUserDialog;
import com.ilyap.yuta.utils.RequestViewModel;

public class ProfileFragment extends Fragment {
    protected View view;
    protected View progressLayout;
    protected User user;
    protected RequestViewModel viewModel;
    protected boolean fromTeams;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        if (getArguments() != null) {
            fromTeams = getArguments().getBoolean("fromTeams", false);
        }

        progressLayout = view.findViewById(R.id.progressLayout);

        viewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        updateProfile();

        view.findViewById(R.id.log_out).setOnClickListener(v -> logOut(requireActivity()));
        view.findViewById(R.id.reload).setOnClickListener(v -> openReloadDialog());
        view.findViewById(R.id.edit).setOnClickListener(v -> openEditUserDialog());
        view.findViewById(R.id.photo).setOnClickListener(v -> openPhotoDialog());

        return view;
    }

    public void updateProfile() {
        updateProfile(getUserId(requireActivity()));
    }

    protected void updateProfile(int userId) {
        progressLayout.setVisibility(VISIBLE);
        viewModel.getResultLiveData().removeObservers(getViewLifecycleOwner());
        if (userId >= 0) {
            viewModel.getUser(userId);
            viewModel.getResultLiveData().observe(getViewLifecycleOwner(), result -> {
                if (!(result instanceof User)) return;
                user = (User) result;
                updateImage();
                fillViews();
                progressLayout.setVisibility(GONE);
                setCurrentUser(user);
            });
        }
    }

    private void fillViews() {
        String fullName = user.getLastName() + " " + user.getFirstName() + (user.getPatronymic() == null ? "" : " " + user.getPatronymic());
        String faculty = getString(R.string.faculty) + ": " + user.getFaculty();
        String direction = getString(R.string.direction) + ": " + user.getDirection();
        String group = getString(R.string.group) + ": " + user.getGroup();
        String projectsCount = user.getDoneProjectsCount() + "/" + user.getAllProjectsCount();
        String tasksCount = user.getDoneTasksCount() + "/" + user.getAllTasksCount();

        setDataInTextView(R.id.name, fullName);
        setDataInTextView(R.id.age, user.getAge());
        setDataInTextView(R.id.biography, user.getBiography());
        setDataInTextView(R.id.faculty, faculty);
        setDataInTextView(R.id.direction, direction);
        setDataInTextView(R.id.group, group);
        setDataInTextView(R.id.projects_count, projectsCount);
        setDataInTextView(R.id.tasks_count, tasksCount);
        setDataInTextView(R.id.teams_count, String.valueOf(user.getTeamsCount()));
        setDataInTextView(R.id.phone_number, user.getPhoneNumber());
        setDataInTextView(R.id.email, user.geteMail());
        setDataInTextView(R.id.vk, user.getVk());

        contactsContainerVisibility(R.id.phone_number, R.id.email, R.id.vk);
    }

    public void updateImage() {
        loadImage(requireContext(), user.getCroppedPhotoUrl(), view.findViewById(R.id.photo));
    }

    private void contactsContainerVisibility(@NonNull int... fields) {
        boolean isEmpty = true;
        for (int field : fields) {
            if (view.findViewById(field).getVisibility() == VISIBLE) {
                isEmpty = false;
                break;
            }
        }
        view.findViewById(R.id.contacts_container).setVisibility(isEmpty ? GONE : VISIBLE);
    }

    private void openReloadDialog() {
        CustomDialog updateUserDialog = new UpdateUserDialog(view.getContext(), this);
        updateUserDialog.start();
    }

    private void openEditUserDialog() {
        CustomDialog editDialog = new EditUserDialog(view.getContext(), this);
        editDialog.start();
    }

    private void openPhotoDialog() {
        CustomDialog photoDialog = new PhotoDialog(view.getContext(), this);
        photoDialog.start();
    }

    private void setDataInTextView(int id, String text) {
        TextView textView = view.findViewById(id);
        textView.setText(null);
        if (text != null) {
            textView.setText(text);
            if (textView.getVisibility() != VISIBLE) {
                textView.setVisibility(VISIBLE);
            }
        } else {
            textView.setVisibility(GONE);
        }
    }

    public final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    UploadPhotoDialog.handleActivityResult(PICK_IMAGE_REQUEST, Activity.RESULT_OK, result.getData());
                }
            }
    );

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (fromTeams) {
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    handleBackPressed();
                }
            };
            requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        }
    }

    protected void handleBackPressed() {
        ((MainActivity) requireActivity()).getNavController().navigate(R.id.teamsFragment);
    }
}