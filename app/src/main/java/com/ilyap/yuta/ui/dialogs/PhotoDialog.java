package com.ilyap.yuta.ui.dialogs;

import android.app.Activity;
import android.content.Context;

import com.ilyap.yuta.R;
import com.ilyap.yuta.ui.fragments.ProfileFragment;

public class PhotoDialog extends CustomInteractiveDialog {
    private final CustomDialog updatePhotoDialog = new UpdatePhotoDialog(activity, profileFragment);

    public PhotoDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.photo_dialog);
    }

    @Override
    public void start() {
        super.start();

        dialog.findViewById(R.id.update_photo).setOnClickListener(v -> {
            openUpdatePhotoDialog();
            this.dismiss();
        });
        dialog.findViewById(R.id.edit_miniature).setOnClickListener(v -> {
            openEditPhotoDialog();
            this.dismiss();
        });
        dialog.findViewById(R.id.delete_photo).setOnClickListener(v -> {
            openDeletePhotoDialog();
            this.dismiss();
        });
    }

    private void openUpdatePhotoDialog() {
        updatePhotoDialog.start();
    }

    private void openEditPhotoDialog() {
        CustomDialog editPhotoDialog = new EditPhotoDialog(activity, profileFragment);
        editPhotoDialog.start();
    }

    private void openDeletePhotoDialog() {
        CustomDialog deletePhotoDialog = new DeletePhotoDialog(activity, profileFragment);
        deletePhotoDialog.start();
    }

    public CustomDialog getUpdatePhotoDialog() {
        return updatePhotoDialog;
    }
}