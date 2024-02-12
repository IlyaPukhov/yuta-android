package com.ilyap.yuta.ui.dialogs.photo;

import static com.ilyap.yuta.utils.RequestUtils.rootUrl;
import static com.ilyap.yuta.utils.UserUtils.getCurrentUser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

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

@SuppressWarnings("ConstantConditions")
public class CropPhotoDialog extends CustomInteractiveDialog {
    private CropImageView cropImageView;

    public CropPhotoDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.crop_photo_dialog);
    }

    @Override
    public void start() {
        super.start();
        User user = getCurrentUser();
        cropImageView = dialog.findViewById(R.id.cropImageView);

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.save_miniature).setOnClickListener(v -> {
            cropPhoto();
            dismiss();
        });

        loadImage(user.getPhotoUrl(), cropImageView);
    }

    private void cropPhoto() {
        int factWidth = cropImageView.getWholeImageRect().width();
        int factHeight = cropImageView.getWholeImageRect().height();
        Rect cropRect = cropImageView.getCropRect();
        RequestUtils.cropUserPhotoRequest(factWidth, factHeight, cropRect.width(), cropRect.height(), cropRect.left, cropRect.top);
        if (fragment != null) {
            ((ProfileFragment) fragment).updateProfile();
        }
    }

    private void loadImage(String path, CropImageView cropImageView) {
        Glide.with(cropImageView)
                .asBitmap()
                .load(rootUrl + path)
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
}