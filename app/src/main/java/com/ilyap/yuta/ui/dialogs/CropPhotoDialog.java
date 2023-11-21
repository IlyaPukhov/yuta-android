package com.ilyap.yuta.ui.dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.canhub.cropper.CropImageView;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestUtils;

public class CropPhotoDialog extends CustomInteractiveDialog {
    private static User user;

    public CropPhotoDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.crop_photo_dialog);
    }

    @Override
    public void start() {
        super.start();

        user = ProfileFragment.getCurrentUser();

        CropImageView cropImageView = dialog.findViewById(R.id.cropImageView);
        View closeButton = dialog.findViewById(R.id.close);
        View saveButton = dialog.findViewById(R.id.save_miniature);

        closeButton.setOnClickListener(v -> dismiss());
        saveButton.setOnClickListener(v -> {
            Rect cropRect = cropImageView.getCropRect();
            RequestUtils.cropPhotoRequest(cropRect.width(), cropRect.height(), cropRect.left, cropRect.top);
            dismiss();
        });

        loadImageWithGlide(user.getPhoto(), cropImageView);
    }

    private void loadImageWithGlide(String imageUrl, CropImageView cropImageView) {
        Glide.with(cropImageView)
                .asBitmap()
                .load(imageUrl)
                .apply(RequestOptions.centerInsideTransform())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                        cropImageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        cropImageView.setImageBitmap(null);
                    }
                });
    }
}