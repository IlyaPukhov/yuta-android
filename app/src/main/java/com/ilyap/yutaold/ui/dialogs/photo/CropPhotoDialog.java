package com.ilyap.yutaold.ui.dialogs.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.canhub.cropper.CropImageView;
import com.ilyap.yutaold.R;
import com.ilyap.yutaold.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yutaold.ui.fragments.ProfileFragment;
import com.ilyap.yuta.network.RequestViewModel;
import com.ilyap.yuta.utils.UserUtils;
import com.ilyap.yuta.domain.model.entity.User;
import com.ilyap.yuta.domain.model.response.UpdateResponse;

import static com.ilyap.yuta.utils.ImageUtils.getConfiguredGlideBuilder;
import static com.ilyap.yuta.utils.UserUtils.getPath;

@SuppressWarnings("ConstantConditions")
public class CropPhotoDialog extends CustomInteractiveDialog {
    private CropImageView cropImageView;
    private RequestViewModel viewModel;

    public CropPhotoDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_crop_photo);
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);
        User userDto = UserUtils.getCurrentUser();

        cropImageView = dialog.findViewById(R.id.cropImageView);

        loadImage(userDto.getPhoto(), cropImageView);

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.save_miniature).setOnClickListener(v -> cropPhoto(userDto.getId()));
    }

    private void cropPhoto(int userId) {
        int factWidth = cropImageView.getWholeImageRect().width();
        int factHeight = cropImageView.getWholeImageRect().height();
        Rect cropRect = cropImageView.getCropRect();

        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.updateMiniatureUserPhoto(userId,
                factWidth, factHeight, cropRect.width(), cropRect.height(), cropRect.left, cropRect.top
        );
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            ((ProfileFragment) fragment).updateProfile();
            dismiss();
        });
    }

    private void loadImage(String path, CropImageView cropImageView) {
        getConfiguredGlideBuilder(Glide.with(cropImageView).asBitmap().load(getPath(path)))
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