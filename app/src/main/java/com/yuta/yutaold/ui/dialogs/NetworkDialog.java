package com.yuta.yutaold.ui.dialogs;

import android.app.Activity;
import com.yuta.yutaold.R;

public class NetworkDialog extends CustomDialog {

    public NetworkDialog(Activity activity) {
        super(activity);
        setDialogLayout(R.layout.dialog_network);
    }
}