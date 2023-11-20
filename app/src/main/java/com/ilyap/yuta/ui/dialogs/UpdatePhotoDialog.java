package com.ilyap.yuta.ui.dialogs;

import android.content.Context;

import com.ilyap.yuta.R;
import com.ilyap.yuta.ui.fragments.ProfileFragment;

public class UpdatePhotoDialog extends CustomInteractiveDialog {

    public UpdatePhotoDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.photo_dialog);
    }

    @Override
    public void start() {
        super.start();
    }
}