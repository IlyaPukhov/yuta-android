package com.ilyap.yuta.ui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import java.util.Objects;

public abstract class CustomInteractiveDialog extends CustomDialog {

    public CustomInteractiveDialog(Context context) {
        super((Activity) context);
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
