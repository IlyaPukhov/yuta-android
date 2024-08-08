package com.ilyap.yutaold.ui.dialogs;

import android.app.Activity;
import com.ilyap.yutaold.R;

public class NetworkDialog extends CustomDialog {

    public NetworkDialog(Activity activity) {
        super(activity);
        setDialogLayout(R.layout.dialog_network);
    }
}