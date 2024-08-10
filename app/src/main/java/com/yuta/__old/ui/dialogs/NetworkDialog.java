package com.yuta.__old.ui.dialogs;

import android.app.Activity;
import com.yuta.__old.R;

public class NetworkDialog extends CustomDialog {

    public NetworkDialog(Activity activity) {
        super(activity);
        setDialogLayout(R.layout.dialog_network);
    }
}