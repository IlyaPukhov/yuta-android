package com.ilyap.yuta.ui.dialogs;

import android.content.Context;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestUtils;

public class DeletePhotoDialog extends CustomInteractiveDialog {

    public DeletePhotoDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.delete_photo_dialog);
    }

    @Override
    public void start() {
        super.start();

        dialog.findViewById(R.id.close).setOnClickListener(v -> this.dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> {
            deletePhoto(profileFragment);
            this.dismiss();
        });
    }

    protected static void deletePhoto(ProfileFragment profileFragment) {
        User user = ProfileFragment.getCurrentUser();
        RequestUtils.deletePhotoRequest(user);
        if (profileFragment != null) {
            profileFragment.fillViews(user);
        }
    }
}