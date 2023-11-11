package com.ilyap.yuta.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.ilyap.yuta.R;

class LoadingDialog {
    private final Activity activity;
    private AlertDialog dialog;

    LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("InflateParams")
    void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog() {
        dialog.dismiss();
    }
}
