package com.ilyap.yuta.ui.dialogs.photo;

import android.content.Context;
import androidx.fragment.app.Fragment;
import com.ilyap.yuta.R;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;

@SuppressWarnings("ConstantConditions")
public class PhotoDialog extends CustomInteractiveDialog {

    public PhotoDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_photo);
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
        CustomDialog updatePhotoDialog = new UploadPhotoDialog(activity, fragment);
        updatePhotoDialog.start();
    }

    private void openEditPhotoDialog() {
        CustomDialog editPhotoDialog = new CropPhotoDialog(activity, fragment);
        editPhotoDialog.start();
    }

    private void openDeletePhotoDialog() {
        CustomDialog deletePhotoDialog = new DeletePhotoDialog(activity, fragment);
        deletePhotoDialog.start();
    }
}