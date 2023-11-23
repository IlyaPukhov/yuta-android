package com.ilyap.yuta.ui.dialogs;

import static com.ilyap.yuta.utils.UserUtils.getCurrentUser;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.ilyap.yuta.R;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestUtils;

public class ReloadDialog extends CustomInteractiveDialog {

    public ReloadDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.reload_dialog);
    }

    @Override
    public void start() {
        super.start();
        EditText password = dialog.findViewById(R.id.submit_password);

        dialog.findViewById(R.id.close).setOnClickListener(v -> this.dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> {
            RequestUtils.reloadRequest(password != null ? password.getText().toString() : null);

            if (profileFragment != null) {
                profileFragment.fillViews(getCurrentUser());
            }

            Toast.makeText(activity, activity.getString(R.string.updated), Toast.LENGTH_SHORT).show();
            this.dismiss();
        });
    }
}