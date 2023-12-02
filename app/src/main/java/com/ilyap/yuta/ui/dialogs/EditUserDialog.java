package com.ilyap.yuta.ui.dialogs;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getCurrentUser;
import static com.ilyap.yuta.utils.UserUtils.getUserId;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.EditResponse;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestViewModel;
import com.santalu.maskara.widget.MaskEditText;

import java.util.regex.Pattern;

@SuppressWarnings("ConstantConditions")
public class EditUserDialog extends CustomInteractiveDialog {
    private static final int PHONE_NUMBER_LENGTH = 10;
    private static final String VK_REGEX = "(https?://)?(www\\.)?vk\\.com/(\\w|\\d)+?/?";
    private boolean isPhoneValid, isEmailValid, isVkValid;
    private RequestViewModel viewModel;
    private EditText biographyView;
    private MaskEditText phoneNumberView;
    private EditText emailView;
    private EditText vkView;
    private TextView error;

    public EditUserDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.edit_user_dialog);
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(profileFragment).get(RequestViewModel.class);

        Button submitButton = dialog.findViewById(R.id.submit);
        biographyView = dialog.findViewById(R.id.biography);
        phoneNumberView = dialog.findViewById(R.id.phone_number);
        emailView = dialog.findViewById(R.id.email);
        vkView = dialog.findViewById(R.id.vk);
        error = dialog.findViewById(R.id.error_text);

        User user = getCurrentUser();
        fillFields(user);
        isPhoneValid = isEmailValid = isVkValid = true;

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        submitButton.setOnClickListener(v -> editUserData(user));

        phoneNumberView.setOnFocusChangeListener((v, hasFocus) -> errorVisibility(hasFocus, isPhoneValid, R.string.error_phone));
        emailView.setOnFocusChangeListener((v, hasFocus) -> errorVisibility(hasFocus, isEmailValid, R.string.error_email));
        vkView.setOnFocusChangeListener((v, hasFocus) -> errorVisibility(hasFocus, isVkValid, R.string.error_vk));
        phoneNumberView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int phoneLength = phoneNumberView.getUnMasked().length();
                isPhoneValid = phoneLength == PHONE_NUMBER_LENGTH || phoneLength == 0;
                submitButton.setEnabled(isPhoneValid);
            }
        });
        emailView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = getData(emailView);
                isEmailValid = email == null || Patterns.EMAIL_ADDRESS.matcher(email).matches();
                submitButton.setEnabled(isEmailValid);
            }
        });
        vkView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String vk = getData(vkView);
                isVkValid = vk == null || Pattern.compile(VK_REGEX).matcher(vk).matches();
                submitButton.setEnabled(isVkValid);
            }
        });
    }

    private void errorVisibility(boolean hasFocus, boolean isValid, int resId) {
        if (!hasFocus || !isValid) {
            error.setText(activity.getString(resId));
            error.setVisibility(VISIBLE);
        } else {
            error.setVisibility(INVISIBLE);
        }
    }

    private void editUserData(User user) {
        setEditUser(user);

        viewModel.getResultLiveData().removeObservers(profileFragment);
        viewModel.editUserData(getUserId(activity), user);
        viewModel.getResultLiveData().observe(profileFragment, result -> {
            if (!(result instanceof EditResponse)) return;
            profileFragment.profileInit();
            dismiss();
        });
    }

    private void setEditUser(User user) {
        user.setBiography(getData(biographyView));

        int phoneLength = phoneNumberView.getUnMasked().length();
        user.setPhoneNumber(phoneLength == 0 ? null : phoneNumberView.getText().toString());

        user.seteMail(getData(emailView));

        user.setVk(getData(vkView));
    }

    private void fillFields(User user) {
        String biographyUser = user.getBiography();
        if (biographyUser != null) {
            biographyView.setText(biographyUser);
        }

        String phoneUser = user.getPhoneNumber();
        if (phoneUser != null) {
            phoneNumberView.setText(phoneUser);
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