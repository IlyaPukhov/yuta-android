package com.ilyap.yuta.ui.dialogs;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestViewModel;

@SuppressWarnings("ConstantConditions")
public class UpdateUserDialog extends CustomInteractiveDialog {
    private RequestViewModel viewModel;
    private TextView errorText;

    public UpdateUserDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.update_user_dialog);
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);

        errorText = dialog.findViewById(R.id.error_text);
        EditText password = dialog.findViewById(R.id.submit_password);

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> updateUserData(password.getText().toString()));
    }

    private void updateUserData(String password) {
        errorText.setVisibility(GONE);
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.updateUserData(getUserId(activity), password);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            if (((UpdateResponse) result).isSuccess()) {
                ((ProfileFragment) fragment).updateProfile();
                dismiss();
            } else {
                errorText.setVisibility(VISIBLE);
            }
        });
    }
}