package com.yuta.profile.ui.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import com.yuta.app.R;
import com.yuta.common.ui.CancelableDialog;
import com.yuta.profile.ui.ProfileFragment;
import com.yuta.app.network.RequestViewModel;
import com.yuta.common.util.UserUtils;
import com.yuta.app.domain.model.entity.User;
import com.yuta.app.domain.model.response.UpdateResponse;
import com.santalu.maskara.widget.MaskEditText;

import java.util.regex.Pattern;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.yuta.common.util.UserUtils.getUserId;

public class EditUserDialog extends CancelableDialog {
    private static final int PHONE_NUMBER_LENGTH = 10;
    private static final String VK_REGEX = "(https?://)?(www\\.)?vk\\.com/(\\w|\\d|[._])+?/?";
    private boolean isPhoneValid, isEmailValid, isVkValid;
    private RequestViewModel viewModel;
    private EditText biographyView;
    private MaskEditText phoneNumberView;
    private EditText emailView;
    private EditText vkView;
    private Button submitButton;
    private TextView errorPhone;
    private TextView errorEmail;
    private TextView errorVk;

    public EditUserDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.dialog_edit_user);
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);

        submitButton = dialog.findViewById(R.id.submit);
        biographyView = dialog.findViewById(R.id.biography);
        phoneNumberView = dialog.findViewById(R.id.phone_number);
        emailView = dialog.findViewById(R.id.email);
        vkView = dialog.findViewById(R.id.vk);
        errorPhone = dialog.findViewById(R.id.error_phone);
        errorEmail = dialog.findViewById(R.id.error_email);
        errorVk = dialog.findViewById(R.id.error_vk);

        User userDto = UserUtils.getCurrentUser();
        fillFields(userDto);

        isPhoneValid = isEmailValid = isVkValid = true;
        setupInputFields();
        setupButtons(userDto);

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
    }

    private void setupInputFields() {
        setupField(phoneNumberView, errorPhone);
        setupField(emailView, errorEmail);
        setupField(vkView, errorVk);
    }

    private void editUserData(User userDto) {
        setEditUser(userDto);

        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.editUserData(getUserId(activity), userDto);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            ((ProfileFragment) fragment).updateProfile();
            dismiss();
        });
    }

    private void setEditUser(@NonNull User userDto) {
        userDto.setBiography(getData(biographyView));
        userDto.setPhoneNumber(getData(phoneNumberView));
        userDto.setEMail(getData(emailView));
        userDto.setVk(getData(vkView));
    }

    private void fillFields(@NonNull User userDto) {
        String biographyUser = userDto.getBiography();
        if (biographyUser != null) {
            biographyView.setText(biographyUser);
        }

        String phoneUser = userDto.getPhoneNumber();
        if (phoneUser != null) {
            phoneNumberView.setText(phoneUser);
        }

        String vkUser = userDto.getVk();
        if (vkUser != null) {
            vkView.setText(vkUser);
        }

        String emailUser = userDto.getEMail();
        if (emailUser != null) {
            emailView.setText(emailUser);
        }
    }

    private String getData(EditText editText) {
        if (editText != null) {
            String text = editText.getText().toString().trim();

            if (!text.isEmpty()) {
                return text;
            }
        }
        return null;
    }

    private void setupField(EditText editText, TextView errorView) {
        setupEditTextValidation(editText, errorView);

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                showError(isFieldValid(editText), errorView);
            }
        });

        if (editText == vkView) {
            editText.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    showError(isFieldValid(editText), errorView);
                    return true;
                }
                return false;
            });
        }
    }

    private void setupButtons(User userDto) {
        submitButton.setOnClickListener(v -> {
            hideKeyboard(vkView);
            editUserData(userDto);
        });
    }

    private boolean isFieldValid(EditText editText) {
        if (editText == phoneNumberView) {
            int phoneLength = phoneNumberView.getUnMasked().length();
            return phoneLength == PHONE_NUMBER_LENGTH || phoneLength == 0;
        } else if (editText == emailView) {
            String email = getData(emailView);
            return email == null || Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else if (editText == vkView) {
            String vk = getData(vkView);
            return vk == null || Pattern.compile(VK_REGEX).matcher(vk).matches();
        }
        return false;
    }

    private void showError(boolean isValid, TextView errorView) {
        if (!isValid) {
            errorView.setVisibility(VISIBLE);
        } else {
            hideError(errorView);
        }
    }

    private void hideError(TextView errorView) {
        errorView.setVisibility(GONE);
    }

    private void setupEditTextValidation(@NonNull EditText editText, TextView errorView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText == phoneNumberView) {
                    int phoneLength = phoneNumberView.getUnMasked().length();
                    isPhoneValid = phoneLength == PHONE_NUMBER_LENGTH || phoneLength == 0;
                } else if (editText == emailView) {
                    String email = getData(emailView);
                    isEmailValid = email == null || Patterns.EMAIL_ADDRESS.matcher(email).matches();
                } else if (editText == vkView) {
                    String vk = getData(vkView);
                    isVkValid = vk == null || Pattern.compile(VK_REGEX).matcher(vk).matches();
                }
                hideError(errorView);
                updateSubmitEnable();
            }
        });
    }

    private void updateSubmitEnable() {
        submitButton.setEnabled(isPhoneValid && isEmailValid && isVkValid);
    }
}