package com.yuta.profile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.yuta.app.R;
import com.yuta.common.ui.AppDialog;
import com.yuta.profile.ui.dialog.PhotoMenuDialog;
import com.yuta.profile.ui.dialog.UploadPhotoDialog;
import com.yuta.profile.ui.dialog.EditUserDialog;
import com.yuta.authorization.ui.LogoutDialog;
import com.yuta.profile.ui.dialog.UpdateUserDialog;
import com.yuta.app.network.RequestViewModel;
import com.yuta.app.domain.model.entity.User;
import com.yuta.app.domain.model.response.UserResponse;
import lombok.NoArgsConstructor;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.yuta.profile.ui.dialog.UploadPhotoDialog.PICK_IMAGE_REQUEST;
import static com.yuta.common.util.GlideUtils.loadImageToImageViewWithoutCaching;
import static com.yuta.common.util.UserUtils.getUserId;
import static com.yuta.common.util.UserUtils.setCurrentUser;

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
    protected User userDto;
    protected ImageView imageView;
    protected RequestViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        setupViews();
        viewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        updateProfile();

        return view;
    }

    private void setupViews() {
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
                userDto = ((UserResponse) result).getUserDto();
                userDto.setId(userId);
                updateImage();
                fillViews();
                progressLayout.setVisibility(GONE);
                imageView.setEnabled(true);
                setCurrentUser(userDto);
            });
        }
    }

    private void fillViews() {
        String fullName = userDto.getLastName() + " " + userDto.getFirstName() + (userDto.getPatronymic() == null ? "" : " " + userDto.getPatronymic());
        String faculty = getString(R.string.faculty) + ": " + userDto.getFaculty();
        String direction = getString(R.string.direction) + ": " + userDto.getDirection();
        String group = getString(R.string.group) + ": " + userDto.getGroup();
        String projectsCount = userDto.getDoneProjectsCount() + "/" + userDto.getAllProjectsCount();
        String tasksCount = userDto.getDoneTasksCount() + "/" + userDto.getAllTasksCount();

        setDataInTextView(R.id.name, fullName);
        setDataInTextView(R.id.age, userDto.getAge());
        setDataInTextView(R.id.biography, userDto.getBiography());
        setDataInTextView(R.id.faculty, faculty);
        setDataInTextView(R.id.direction, direction);
        setDataInTextView(R.id.group, group);
        setDataInTextView(R.id.projects_count, projectsCount);
        setDataInTextView(R.id.tasks_count, tasksCount);
        setDataInTextView(R.id.teams_count, String.valueOf(userDto.getTeamsCount()));
        setDataInTextView(R.id.phone_number, userDto.getPhoneNumber());
        setDataInTextView(R.id.email, userDto.getEMail());
        setDataInTextView(R.id.vk, userDto.getVk());

        contactsContainerVisibility(R.id.phone_number, R.id.email, R.id.vk);
    }

    public void updateImage() {
        loadImageToImageViewWithoutCaching(imageView, userDto.getCroppedPhoto());
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
        AppDialog updateUserDialog = new UpdateUserDialog(view.getContext(), this);
        updateUserDialog.start();
    }

    private void openEditUserDialog() {
        AppDialog editDialog = new EditUserDialog(view.getContext(), this);
        editDialog.start();
    }

    private void openPhotoDialog() {
        AppDialog photoDialog = new PhotoMenuDialog(view.getContext(), this);
        photoDialog.start();
    }

    private void openLogoutDialog() {
        AppDialog logoutDialog = new LogoutDialog(view.getContext(), this);
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
}