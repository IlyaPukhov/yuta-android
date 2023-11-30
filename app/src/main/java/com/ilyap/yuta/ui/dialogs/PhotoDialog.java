package com.ilyap.yuta.ui.dialogs;

import android.content.Context;

import com.ilyap.yuta.R;
import com.ilyap.yuta.ui.fragments.ProfileFragment;

@SuppressWarnings("ConstantConditions")
public class PhotoDialog extends CustomInteractiveDialog {
    private final CustomDialog updatePhotoDialog = new UploadPhotoDialog(activity, profileFragment);

    public PhotoDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.photo_dialog);
    }

    @Override
    public void start() {
        super.start();

        dialog.findViewById(R.id.update_photo).setOnClickListener(v -> {
            openUpdatePhotoDialog();
            dismiss();
        });
        dialog.findViewById(R.id.edit_miniature).setOnClickListener(v -> {
            openEditPhotoDialog();
            dismiss();
        });
        dialog.findViewById(R.id.delete_photo).setOnClickListener(v -> {
            openDeletePhotoDialog();
            dismiss();
        });
    }

    private void openUpdatePhotoDialog() {
        updatePhotoDialog.start();
    }

    private void openEditPhotoDialog() {
        CustomDialog editPhotoDialog = new CropPhotoDialog(activity, profileFragment);
        editPhotoDialog.start();
    }

    private void openDeletePhotoDialog() {
        CustomDialog deletePhotoDialog = new DeletePhotoDialog(activity, profileFragment);
        deletePhotoDialog.start();
    }
}