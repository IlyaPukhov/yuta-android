package com.ilyap.yutaold.ui.dialogs;

import android.app.Activity;
import com.ilyap.yutaold.R;

public class LoadingDialog extends CustomDialog {

    public LoadingDialog(Activity activity) {
        super(activity);
        setDialogLayout(R.layout.dialog_loading);
    }
}