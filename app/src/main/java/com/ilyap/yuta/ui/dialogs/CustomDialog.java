package com.ilyap.yuta.ui.dialogs;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.Objects;

public abstract class CustomDialog extends AlertDialog {
    protected final Activity activity;
    protected AlertDialog dialog;
    protected int dialogLayout;

    public CustomDialog(@NonNull Activity activity) {
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

    public void hideKeyboard(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), HIDE_NOT_ALWAYS);
        view.clearFocus();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    protected void setDialogLayout(int dialogLayout) {
        this.dialogLayout = dialogLayout;
    }
}