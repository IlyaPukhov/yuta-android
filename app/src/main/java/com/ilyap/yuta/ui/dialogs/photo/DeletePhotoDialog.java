package com.ilyap.yuta.ui.dialogs.photo;

import static com.ilyap.yuta.utils.UserUtils.getCurrentUser;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestUtils;

@SuppressWarnings("ConstantConditions")
public class DeletePhotoDialog extends CustomInteractiveDialog {

    public DeletePhotoDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_delete);
    }

    @Override
    public void start() {
        super.start();

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> {
            deletePhoto(fragment);
            dismiss();
        });
    }

    protected void deletePhoto(Fragment fragment) {
        User user = getCurrentUser();
        RequestUtils.deleteUserPhotoRequest(user);
        if (fragment != null) {
            ((ProfileFragment) fragment).updateProfile();
        }
    }
}