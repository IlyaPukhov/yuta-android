package com.ilyap.yuta.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestUtils;

public class UploadPhotoDialog extends CustomInteractiveDialog {
    public static final int PICK_IMAGE_REQUEST = 1;
    @SuppressLint("StaticFieldLeak")
    private static ImageView imageView;
    private static Uri selectedImageUri;
    private static User user;

    public UploadPhotoDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.upload_photo_dialog);
    }

    @Override
    public void start() {
        super.start();
        user = ProfileFragment.getCurrentUser();

        imageView = dialog.findViewById(R.id.photo);
        Glide.with(activity).load(user.getCroppedPhoto())
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);

        dialog.findViewById(R.id.close).setOnClickListener(v -> this.dismiss());
        dialog.findViewById(R.id.delete_photo).setOnClickListener(v -> {
            DeletePhotoDialog.deletePhoto(profileFragment);
            imageView.setImageBitmap(null);
        });
        dialog.findViewById(R.id.pick_miniature).setOnClickListener(v -> {
            CustomDialog editPhotoDialog = new CropPhotoDialog(activity, profileFragment);
            editPhotoDialog.start();
            this.dismiss();
        });
        dialog.findViewById(R.id.pick_photo).setOnClickListener(v -> pickPhoto());
    }

    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        profileFragment.imagePickerLauncher.launch(Intent.createChooser(intent, activity.getString(R.string.pick_image)));
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            updatePhoto();
        }
    }

    private static void updatePhoto() {
        if (imageView != null && selectedImageUri != null) {
            user.setPhoto(String.valueOf(selectedImageUri));
            user.setCroppedPhoto(String.valueOf(selectedImageUri));
            if (profileFragment != null) {
                profileFragment.updateImage(user);
            }
            RequestUtils.uploadPhotoRequest(user);
            imageView.setImageURI(selectedImageUri);
        }
    }
}