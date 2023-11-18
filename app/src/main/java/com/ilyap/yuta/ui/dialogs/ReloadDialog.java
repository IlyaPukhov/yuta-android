package com.ilyap.yuta.ui.dialogs;

import android.content.Context;
import android.widget.EditText;

import com.ilyap.yuta.R;
import com.ilyap.yuta.utils.RequestUtils;

public class ReloadDialog extends CustomInteractiveDialog {

    public ReloadDialog(Context context) {
        super(context);
        setDialogLayout(R.layout.reload_dialog);
    }

    @Override
    public void start() {
        super.start();
        EditText password = (EditText) dialog.findViewById(R.id.submit_password);

        dialog.findViewById(R.id.close).setOnClickListener(v -> this.dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> {
            RequestUtils.reloadRequest(password != null ? password.getText().toString() : null);
            this.dismiss();
        });
    }
}