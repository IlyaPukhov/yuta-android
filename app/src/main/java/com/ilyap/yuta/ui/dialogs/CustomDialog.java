package com.ilyap.yuta.ui.dialogs;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import java.util.Objects;

public abstract class CustomDialog extends AlertDialog {
    protected final Activity activity;
    protected AlertDialog dialog;
    protected int dialogLayout;

    public CustomDialog(Activity activity) {
        super(activity.getApplicationContext());
        this.activity = activity;
    }

    public void start() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(dialogLayout, null));
        builder.setCancelable(false);

        dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    protected void setDialogLayout(int dialogLayout) {
        this.dialogLayout = dialogLayout;
    }
}