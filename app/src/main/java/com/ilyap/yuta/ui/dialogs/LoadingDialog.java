package com.ilyap.yuta.ui.dialogs;

import android.app.Activity;

import com.ilyap.yuta.R;

public class LoadingDialog extends CustomDialog {

    public LoadingDialog(Activity activity) {
        super(activity);
        setDialogLayout(R.layout.loading_dialog);
    }
}