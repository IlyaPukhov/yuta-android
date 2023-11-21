package com.ilyap.yuta.ui.dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.canhub.cropper.CropImageView;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class CropPhotoDialog extends CustomInteractiveDialog {
    CropImageView cropImageView;
    private User user;

    public CropPhotoDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.crop_photo_dialog);
    }

    @Override
    public void start() {
        super.start();

        user = ProfileFragment.getCurrentUser();
        cropImageView = dialog.findViewById(R.id.cropImageView);

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.save_miniature).setOnClickListener(v -> {
            Rect cropRect = cropImageView.getCropRect();
            RequestUtils.cropPhotoRequest(cropRect.width(), cropRect.height(), cropRect.left, cropRect.top);
            updateLocalImage();
            this.dismiss();
        });

        loadImageWithGlide(user.getPhoto(), cropImageView);
    }

    private void updateLocalImage() {
        // TODO убрать
        user.setCroppedPhoto(Objects.requireNonNull(saveCroppedPhoto().getPath()));

        if (profileFragment != null) {
            profileFragment.updateImage(user);
        }
    }

    private void loadImageWithGlide(String imageUrl, CropImageView cropImageView) {
        Glide.with(cropImageView)
                .asBitmap()
                .load(imageUrl)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
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

    private Uri saveCroppedPhoto() {
        File outputDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File outputFile = new File(outputDir, "cropped_image.jpg");

        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            cropImageView.getCroppedImage().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Uri.fromFile(outputFile);
    }
}