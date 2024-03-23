package com.ilyap.yuta.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.ilyap.yuta.models.UserResponse;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.photo.PhotoDialog;
import com.ilyap.yuta.ui.dialogs.photo.UploadPhotoDialog;
import com.ilyap.yuta.ui.dialogs.user.EditUserDialog;
import com.ilyap.yuta.ui.dialogs.user.LogoutDialog;
import com.ilyap.yuta.ui.dialogs.user.UpdateUserDialog;
import com.ilyap.yuta.utils.RequestViewModel;
import lombok.NoArgsConstructor;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.ui.dialogs.photo.UploadPhotoDialog.PICK_IMAGE_REQUEST;
import static com.ilyap.yuta.utils.UserUtils.getUserId;
import static com.ilyap.yuta.utils.UserUtils.loadImage;
import static com.ilyap.yuta.utils.UserUtils.setCurrentUser;

@NoArgsConstructor
public class ProfileFragment extends Fragment {
    public final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    UploadPhotoDialog.handleActivityResult(PICK_IMAGE_REQUEST, Activity.RESULT_OK, result.getData());
                }
            }
    );
    protected View view;
    protected View progressLayout;
    protected User user;
    protected ImageView imageView;
    protected RequestViewModel viewModel;
    protected int fromFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        if (getArguments() != null) {
            fromFragment = getArguments().getInt("fromFragment", -1);
        }

        setupViews();
        viewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        updateProfile();

        return view;
    }

    private void setupViews() {
        if (fromFragment > 0) {
            View backButton = view.findViewById(R.id.back_button);
            backButton.setVisibility(VISIBLE);
            backButton.setOnClickListener(v -> handleBackPressed());
        }
        progressLayout = view.findViewById(R.id.progressLayout);
        imageView = view.findViewById(R.id.photo);
        view.findViewById(R.id.log_out).setOnClickListener(v -> openLogoutDialog());
        view.findViewById(R.id.reload).setOnClickListener(v -> openReloadDialog());
        view.findViewById(R.id.edit).setOnClickListener(v -> openEditUserDialog());
        imageView.setOnClickListener(v -> openPhotoDialog());
    }

    public void updateProfile() {
        updateProfile(getUserId(requireActivity()));
    }

    protected void updateProfile(int userId) {
        progressLayout.setVisibility(VISIBLE);
        imageView.setEnabled(false);
        viewModel.getResultLiveData().removeObservers(getViewLifecycleOwner());
        if (userId >= 0) {
            viewModel.getUser(userId);
            viewModel.getResultLiveData().observe(getViewLifecycleOwner(), result -> {
                if (!(result instanceof UserResponse)) return;
                user = ((UserResponse) result).getUser();
                updateImage();
                fillViews();
                progressLayout.setVisibility(GONE);
                imageView.setEnabled(true);
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
        setDataInTextView(R.id.email, user.getEMail());
        setDataInTextView(R.id.vk, user.getVk());

        contactsContainerVisibility(R.id.phone_number, R.id.email, R.id.vk);
    }

    public void updateImage() {
        loadImage(requireContext(), user.getCroppedPhotoUrl(), imageView);
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

    private void openLogoutDialog() {
        CustomDialog logoutDialog = new LogoutDialog(view.getContext(), this);
        logoutDialog.start();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (fromFragment > 0) {
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
        ((MainActivity) requireActivity()).getNavController().navigate(fromFragment);
    }
}