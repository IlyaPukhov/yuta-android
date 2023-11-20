package com.ilyap.yuta.ui.dialogs;

import android.content.Context;

import com.ilyap.yuta.R;
import com.ilyap.yuta.ui.fragments.ProfileFragment;

public class EditPhotoDialog extends CustomInteractiveDialog {

    public EditPhotoDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.photo_dialog);
    }

    @Override
    public void start() {
        super.start();
    }
}