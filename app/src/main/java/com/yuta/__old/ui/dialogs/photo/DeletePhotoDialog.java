package com.yuta.__old.ui.dialogs.photo;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.yuta.__old.R;
import com.yuta.app.domain.model.response.UpdateResponse;
import com.yuta.__old.ui.dialogs.CustomInteractiveDialog;
import com.yuta.__old.ui.fragments.ProfileFragment;
import com.yuta.app.network.RequestViewModel;
import com.yuta.app.util.UserUtils;

@SuppressWarnings("ConstantConditions")
public class DeletePhotoDialog extends CustomInteractiveDialog {
    private RequestViewModel viewModel;

    public DeletePhotoDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_delete);
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> {
            deletePhoto(fragment);
            dismiss();
        });
    }

    protected void deletePhoto(Fragment fragment) {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.deleteUserPhoto(UserUtils.getCurrentUser().getId());
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            ((ProfileFragment) fragment).updateProfile();
            dismiss();
        });
    }
}