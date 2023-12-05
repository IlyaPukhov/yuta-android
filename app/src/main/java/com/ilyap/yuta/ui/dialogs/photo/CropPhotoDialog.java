package com.ilyap.yuta.ui.dialogs.photo;

import static com.ilyap.yuta.utils.UserUtils.getCurrentUser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.canhub.cropper.CropImageView;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@SuppressWarnings("ConstantConditions")
public class CropPhotoDialog extends CustomInteractiveDialog {
    private CropImageView cropImageView;
    private User user;

    public CropPhotoDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.crop_photo_dialog);
    }

    @Override
    public void start() {
        super.start();

        user = getCurrentUser();
        cropImageView = dialog.findViewById(R.id.cropImageView);

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.save_miniature).setOnClickListener(v -> {
            int factWidth = cropImageView.getWholeImageRect().width();
            int factHeight = cropImageView.getWholeImageRect().height();
            Rect cropRect = cropImageView.getCropRect();
            RequestUtils.cropUserPhotoRequest(factWidth, factHeight, cropRect.width(), cropRect.height(), cropRect.left, cropRect.top);
            updateLocalImage();

            dismiss();
        });

        loadImage(user.getPhoto(), cropImageView);
    }

    private void updateLocalImage() {
        // TODO убрать
        user.setCroppedPhoto(Objects.requireNonNull(saveCroppedPhoto().getPath()));

        if (fragment != null) {
            ((ProfileFragment) fragment).updateImage(user);
        }
    }

    private void loadImage(String imagePath, CropImageView cropImageView) {
        Glide.with(cropImageView)
                .asBitmap()
                .load(imagePath)
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