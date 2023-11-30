package com.ilyap.yuta.ui.dialogs;

import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.EMPTY_DATA;
import static com.ilyap.yuta.utils.UserUtils.getCurrentUser;
import static com.ilyap.yuta.utils.UserUtils.getUserId;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.EditResponse;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestViewModel;
import com.santalu.maskara.widget.MaskEditText;

@SuppressWarnings("ConstantConditions")
public class EditUserDialog extends CustomInteractiveDialog {
    private static final int PHONE_NUMBER_LENGTH = 10;
    private RequestViewModel viewModel;
    private EditText biographyView;
    private MaskEditText phoneNumberView;
    private EditText emailView;
    private EditText vkView;

    public EditUserDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.edit_user_dialog);
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(profileFragment).get(RequestViewModel.class);

        biographyView = dialog.findViewById(R.id.biography);
        phoneNumberView = dialog.findViewById(R.id.phone_number);
        emailView = dialog.findViewById(R.id.email);
        vkView = dialog.findViewById(R.id.vk);

        User user = getCurrentUser();
        fillFields(user);

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> editUserData(user));
    }

    private void editUserData(User user) {
        if (editUser(user)) {
            viewModel.getResultLiveData().removeObservers(profileFragment);
            viewModel.editUserData(getUserId(activity), user);
            viewModel.getResultLiveData().observe(profileFragment, result -> {
                if (!(result instanceof EditResponse)) return;
                profileFragment.profileInit();
                dismiss();
            });
        }
    }

    private boolean editUser(User user) {
        boolean isValid = true;
        TextView error = dialog.findViewById(R.id.error_text);

        String biography = getData(biographyView);
        user.setBiography(biography != null ? biography : EMPTY_DATA);

        int phoneLength = phoneNumberView.getUnMasked().length();
        if (phoneLength == PHONE_NUMBER_LENGTH || phoneLength == 0) {
            user.setPhoneNumber(phoneNumberView.getText().toString());
        } else {
            error.setText(activity.getString(R.string.error_phone));
            error.setVisibility(VISIBLE);
            isValid = false;
        }

        String email = getData(emailView);
        if (email != null) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                user.seteMail(email);
            } else {
                error.setText(activity.getString(R.string.error_email));
                error.setVisibility(VISIBLE);
                isValid = false;
            }
        } else {
            user.seteMail(EMPTY_DATA);
        }

        String vk = getData(vkView);
        user.setVk(vk != null ? vk : EMPTY_DATA);

        return isValid;
    }

    private void fillFields(User user) {
        String biographyUser = user.getBiography();
        if (biographyUser != null) {
            biographyView.setText(biographyUser);
        }

        String phoneUser = user.getPhoneNumber();
        if (phoneUser != null) {
            phoneNumberView.setText(phoneUser);
            phoneNumberView.getEditableText().replace(0, phoneNumberView.length(), phoneUser);
//            phoneNumberView.setSelection(phoneNumberView.getText().length());
        }

        String vkUser = user.getVk();
        if (vkUser != null) {
            vkView.setText(vkUser);
        }

        String emailUser = user.geteMail();
        if (emailUser != null) {
            emailView.setText(emailUser);
        }
    }

    private String getData(EditText editText) {
        if (editText != null) {
            String text = editText.getText().toString().trim();

            if (!text.equals("")) {
                return text;
            }
        }
        return null;
    }
}