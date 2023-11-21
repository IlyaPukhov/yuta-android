package com.ilyap.yuta.ui.dialogs;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestUtils;
import com.santalu.maskara.widget.MaskEditText;

public class EditUserDialog extends CustomInteractiveDialog {
    private static final int PHONE_NUMBER_LENGTH = 10;
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
        biographyView = dialog.findViewById(R.id.biography);
        phoneNumberView = dialog.findViewById(R.id.phone_number);
        emailView = dialog.findViewById(R.id.email);
        vkView = dialog.findViewById(R.id.vk);

        User user = ProfileFragment.getCurrentUser();
        fillFields(user);

        dialog.findViewById(R.id.close).setOnClickListener(v -> this.dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> {
            if (editUser(user)) {
                RequestUtils.editUserRequest(user);
                if (profileFragment != null) {
                    profileFragment.fillViews(user);
                }

                Toast.makeText(activity, activity.getString(R.string.updated), Toast.LENGTH_SHORT).show();
                this.dismiss();
            }
        });
    }

    private boolean editUser(User user) {
        boolean isCorrect = true;
        TextView error = dialog.findViewById(R.id.error_text);

        String biography = getData(biographyView);
        if (biography != null) {
            user.setBiography(biography);
        }

        int phoneLength = phoneNumberView.getUnMasked().length();
        if (phoneLength != 0) {
            if (phoneLength == PHONE_NUMBER_LENGTH) {
                user.setPhoneNumber(phoneNumberView.getMasked());
            } else {
                error.setText(activity.getString(R.string.error_phone));
                error.setVisibility(VISIBLE);
                isCorrect = false;
            }
        }

        String email = getData(emailView);
        if (email != null) {
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                user.seteMail(email);
            } else {
                error.setText(activity.getString(R.string.error_email));
                error.setVisibility(VISIBLE);
                isCorrect = false;
            }
        }

        String vk = getData(vkView);
        if (vk != null) {
            user.setVk(vk);
        }
        return isCorrect;
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