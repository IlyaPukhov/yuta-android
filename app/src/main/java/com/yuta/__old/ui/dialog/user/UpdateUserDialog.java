package com.yuta.__old.ui.dialog.user;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import com.yuta.__old.R;
import com.yuta.app.domain.model.response.UpdateResponse;
import com.yuta.common.ui.InteractiveDialog;
import com.yuta.__old.ui.fragment.ProfileFragment;
import com.yuta.app.network.RequestViewModel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.yuta.common.util.UserUtils.getUserId;

@SuppressWarnings("ConstantConditions")
public class UpdateUserDialog extends InteractiveDialog {
    private RequestViewModel viewModel;
    private TextView errorText;
    private Button submitButton;

    public UpdateUserDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.dialog_update_user);
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);

        errorText = dialog.findViewById(R.id.error_text);
        EditText password = dialog.findViewById(R.id.submit_password);
        setupEditView(password);

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        submitButton = dialog.findViewById(R.id.submit);
        submitButton.setOnClickListener(v -> {
            hideKeyboard(password);
            updateUserData(password.getText().toString());
        });
    }

    private void updateUserData(String password) {
        errorText.setVisibility(GONE);
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.updateUserData(getUserId(activity), password);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            if (((UpdateResponse) result).getStatus().equals("OK")) {
                ((ProfileFragment) fragment).updateProfile();
                dismiss();
            } else {
                errorText.setVisibility(VISIBLE);
            }
        });
    }

    private void setupEditView(@NonNull EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorText.setVisibility(GONE);
                submitButton.setEnabled(!s.toString().trim().isEmpty());
            }
        });
    }
}