package com.ilyap.yuta.ui.dialogs;

import android.app.Activity;
import com.ilyap.yuta.R;

public class NetworkDialog extends CustomDialog {

    public NetworkDialog(Activity activity) {
        super(activity);
        setDialogLayout(R.layout.dialog_network);
    }
}