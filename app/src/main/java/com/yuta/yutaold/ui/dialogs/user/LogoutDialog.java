package com.yuta.yutaold.ui.dialogs.user;

import android.content.Context;
import androidx.fragment.app.Fragment;
import com.yuta.yutaold.R;
import com.yuta.yutaold.ui.dialogs.CustomInteractiveDialog;
import com.yuta.app.utils.UserUtils;

@SuppressWarnings("ConstantConditions")
public class LogoutDialog extends CustomInteractiveDialog {

    public LogoutDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_logout);
    }

    @Override
    public void start() {
        super.start();

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> {
            UserUtils.logOut(activity);
            dismiss();
        });
    }
}