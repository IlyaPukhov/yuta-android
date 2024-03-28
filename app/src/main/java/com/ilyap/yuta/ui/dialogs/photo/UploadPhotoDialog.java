package com.ilyap.yuta.ui.dialogs.photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.FileUtils;
import com.ilyap.yuta.utils.RequestViewModel;
import lombok.SneakyThrows;

import java.io.InputStream;

import static com.ilyap.yuta.ui.dialogs.photo.PhotoDialog.DEFAULT_USER_PHOTO;
import static com.ilyap.yuta.utils.UserUtils.getCurrentUser;
import static com.ilyap.yuta.utils.UserUtils.loadImageToImageView;

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
        User user = getCurrentUser();

        imageView = dialog.findViewById(R.id.photo);
        loadImageToImageView(imageView, user.getCroppedPhotoUrl());

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.delete_photo).setOnClickListener(v -> loadImageToImageView(imageView, user.getCroppedPhotoUrl()));
        dialog.findViewById(R.id.pick_miniature).setOnClickListener(v -> {
            if (selectedImageUri == null && user.getPhotoUrl().equals(DEFAULT_USER_PHOTO)) return;
            updatePhoto(user);
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
    private void updatePhoto(User user) {
        InputStream inputStream = fragment.requireContext().getContentResolver().openInputStream(selectedImageUri);
        String filename = FileUtils.getFileName(getContext(), selectedImageUri);

        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.updateUserPhoto(user.getId(), inputStream, filename);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            CustomDialog cropPhotoDialog = new CropPhotoDialog(activity, fragment);
            cropPhotoDialog.start();
            dismiss();
        });
    }

    private static void setImage(Uri uri) {
        selectedImageUri = uri;
        imageView.setImageURI(selectedImageUri);
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            setImage(data.getData());
        }
    }
}