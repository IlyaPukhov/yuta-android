package com.ilyap.yuta.ui.dialogs.photo;

import static com.ilyap.yuta.utils.UserUtils.getCurrentUser;
import static com.ilyap.yuta.utils.UserUtils.loadImage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestUtils;

@SuppressWarnings("ConstantConditions")
public class UploadPhotoDialog extends CustomInteractiveDialog {
    @SuppressLint("StaticFieldLeak")
    private static ImageView imageView;
    public static final int PICK_IMAGE_REQUEST = 1;
    private static Uri selectedImageUri;
    private static User user;

    public UploadPhotoDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.upload_photo_dialog);
    }

    @Override
    public void start() {
        super.start();
        user = getCurrentUser();

        imageView = dialog.findViewById(R.id.photo);
        loadImage(activity, user.getCroppedPhoto(), imageView);

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.delete_photo).setOnClickListener(v -> {
            DeletePhotoDialog.deletePhoto(fragment);
            imageView.setImageBitmap(null);
        });
        dialog.findViewById(R.id.pick_miniature).setOnClickListener(v -> {
            CustomDialog editPhotoDialog = new CropPhotoDialog(activity, fragment);
            editPhotoDialog.start();
            dismiss();
        });
        dialog.findViewById(R.id.pick_photo).setOnClickListener(v -> pickPhoto());
    }

    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((ProfileFragment) fragment).imagePickerLauncher.launch(Intent.createChooser(intent, activity.getString(R.string.pick_image)));
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            updatePhoto();
        }
    }

    private static void updatePhoto() {
        if (selectedImageUri != null) {
            user.setPhoto(String.valueOf(selectedImageUri));
            user.setCroppedPhoto(String.valueOf(selectedImageUri));
            RequestUtils.uploadUserPhotoRequest(user);
            imageView.setImageURI(selectedImageUri);
            if (fragment != null) {
                ((ProfileFragment) fragment).updateImage(user);
            }
        }
    }
}