package com.yuta.__old.ui.dialog.photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.yuta.__old.R;
import com.yuta.common.ui.CustomDialog;
import com.yuta.common.ui.CustomInteractiveDialog;
import com.yuta.__old.ui.fragment.ProfileFragment;
import com.yuta.common.util.FileUtils;
import com.yuta.app.network.RequestViewModel;
import com.yuta.common.util.UserUtils;
import com.yuta.app.domain.model.entity.User;
import com.yuta.app.domain.model.response.UpdateResponse;
import com.yuta.app.domain.model.response.UserResponse;
import lombok.SneakyThrows;

import java.io.InputStream;

import static com.yuta.__old.ui.dialog.photo.PhotoDialog.DEFAULT_USER_PHOTO;
import static com.yuta.common.util.GlideUtils.loadImageToImageViewWithoutCaching;

@SuppressWarnings("ConstantConditions")
public class UploadPhotoDialog extends CustomInteractiveDialog {
    public static final int PICK_IMAGE_REQUEST = 1;
    @SuppressLint("StaticFieldLeak")
    private static ImageView imageView;
    private static Uri selectedImageUri;
    private RequestViewModel viewModel;

    public UploadPhotoDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_upload_photo);
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);
        User userDto = UserUtils.getCurrentUser();

        imageView = dialog.findViewById(R.id.photo);
        loadImageToImageViewWithoutCaching(imageView, userDto.getCroppedPhoto());

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.delete_photo).setOnClickListener(v -> loadImageToImageViewWithoutCaching(imageView, userDto.getCroppedPhoto()));
        dialog.findViewById(R.id.pick_miniature).setOnClickListener(v -> {
            if (userDto.getPhoto().equals(DEFAULT_USER_PHOTO) && selectedImageUri == null) return;
            if (selectedImageUri != null) {
                updatePhoto(userDto.getId());
            } else {
                openCropDialog();
            }

        });
        dialog.findViewById(R.id.pick_photo).setOnClickListener(v -> pickPhoto());
    }

    private void pickPhoto() {
        String[] mimeTypes = {"image/jpeg", "image/png"};
        Intent intent = new Intent();
        intent.setType("image/*")
                .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((ProfileFragment) fragment).imagePickerLauncher.launch(Intent.createChooser(intent, activity.getString(R.string.pick_image)));
    }

    @SneakyThrows
    private void updatePhoto(int userId) {
        InputStream inputStream = fragment.requireContext().getContentResolver().openInputStream(selectedImageUri);
        String filename = FileUtils.getFilename(getContext(), selectedImageUri);

        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.updateUserPhoto(userId, FileUtils.rotateImage(inputStream, filename), filename);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            reloadUserData(userId);
            openCropDialog();
        });
    }

    private static void setImage(Uri uri) {
        selectedImageUri = uri;
        imageView.setImageURI(selectedImageUri);
    }

    private void reloadUserData(int userId) {
        viewModel.getResultLiveData().removeObservers(fragment.getViewLifecycleOwner());
        viewModel.getUser(userId);
        viewModel.getResultLiveData().observe(fragment.getViewLifecycleOwner(), result -> {
            if (!(result instanceof UserResponse)) return;
            ((ProfileFragment) fragment).updateProfile();
        });
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            setImage(data.getData());
        }
    }

    private void openCropDialog() {
        CustomDialog cropPhotoDialog = new CropPhotoDialog(activity, fragment);
        cropPhotoDialog.start();
        dismiss();
    }
}