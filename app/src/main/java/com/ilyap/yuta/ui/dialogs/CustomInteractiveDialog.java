package com.ilyap.yuta.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;


import com.ilyap.yuta.ui.fragments.ProfileFragment;

import java.util.Objects;

public abstract class CustomInteractiveDialog extends CustomDialog {
    @SuppressLint("StaticFieldLeak")
    protected static ProfileFragment profileFragment;

    public CustomInteractiveDialog(Context context, ProfileFragment profileFragment) {
        super((Activity) context);
        CustomInteractiveDialog.profileFragment = profileFragment;
    }

    @Override
    public void start() {
        Builder builder = new Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(dialogLayout, null));

        dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
