package com.yuta.__old.ui.dialogs.photo;

import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.yuta.__old.R;
import com.yuta.__old.ui.dialogs.CustomDialog;
import com.yuta.__old.ui.dialogs.CustomInteractiveDialog;
import com.yuta.common.util.UserUtils;

import static android.view.View.GONE;

@SuppressWarnings("ConstantConditions")
public class PhotoDialog extends CustomInteractiveDialog {

    public static final String DEFAULT_USER_PHOTO = "default-user-photo";

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

        View editMiniature = dialog.findViewById(R.id.edit_miniature);
        View deletePhoto = dialog.findViewById(R.id.delete_photo);
        if (UserUtils.getCurrentUser().getPhoto().contains(DEFAULT_USER_PHOTO)) {
            editMiniature.setVisibility(GONE);
            deletePhoto.setVisibility(GONE);
        } else {
            editMiniature.setOnClickListener(v -> {
                openEditPhotoDialog();
                dismiss();
            });

            deletePhoto.setOnClickListener(v -> {
                openDeletePhotoDialog();
                dismiss();
            });
        }
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