package com.ilyap.yuta.ui.dialogs.photo;

import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.ilyap.yuta.R;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;

import static android.view.View.GONE;
import static com.ilyap.yuta.utils.UserUtils.getCurrentUser;

@SuppressWarnings("ConstantConditions")
public class PhotoDialog extends CustomInteractiveDialog {

    public static final String DEFAULT_USER_PHOTO = "default_user_photo";

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
        if (getCurrentUser().getPhotoUrl().contains(DEFAULT_USER_PHOTO)) {
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