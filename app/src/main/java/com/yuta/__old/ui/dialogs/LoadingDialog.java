package com.yuta.__old.ui.dialogs;

import android.app.Activity;
import com.yuta.__old.R;

public class LoadingDialog extends CustomDialog {

    public LoadingDialog(Activity activity) {
        super(activity);
        setDialogLayout(R.layout.dialog_loading);
    }
}