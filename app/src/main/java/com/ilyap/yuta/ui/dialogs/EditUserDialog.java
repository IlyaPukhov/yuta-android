package com.ilyap.yuta.ui.dialogs;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.fragments.ProfileFragment;
import com.ilyap.yuta.utils.RequestUtils;
import com.santalu.maskara.widget.MaskEditText;

public class EditUserDialog extends CustomInteractiveDialog {
    private static final int PHONE_NUMBER_LENGTH = 10;
    private User user;

    public EditUserDialog(Context context, ProfileFragment profileFragment) {
        super(context, profileFragment);
        setDialogLayout(R.layout.edit_user_dialog);
    }

    @Override
    public void start() {
        super.start();
        user = ProfileFragment.getCurrentUser();

        dialog.findViewById(R.id.close).setOnClickListener(v -> this.dismiss());
        dialog.findViewById(R.id.submit).setOnClickListener(v -> {
            if (editUser()) {
                RequestUtils.editUserRequest(user);
                if (profileFragment != null) {
                    profileFragment.fillViews(user);
                }
                this.dismiss();
            }
        });
    }

    private boolean editUser() {
        boolean isCorrect = true;

        String biography = getData(R.id.biography);
        MaskEditText editTextPhoneNumber = dialog.findViewById(R.id.phone_number);
        String email = getData(R.id.email);
        String vk = getData(R.id.vk);
        TextView error = (TextView) dialog.findViewById(R.id.error_text);

        if (biography != null) {
            user.setBiography(biography);
        }

        if (vk != null) {
            user.setVk(vk);
        }

        if (email != null) {
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                user.seteMail(email);
            } else {
                error.setText(activity.getString(R.string.error_email));
                error.setVisibility(VISIBLE);
                isCorrect = false;
            }
        }

        int phoneLength = editTextPhoneNumber.getUnMasked().length();
        if (phoneLength != 0) {
            if (phoneLength == PHONE_NUMBER_LENGTH) {
                user.setPhoneNumber(editTextPhoneNumber.getMasked());
            } else {
                error.setText(activity.getString(R.string.error_phone));
                error.setVisibility(VISIBLE);
                isCorrect = false;
            }
        }
        return isCorrect;
    }

    private String getData(int id) {
        EditText editText = (EditText) dialog.findViewById(id);

        if (editText != null) {
            String text = editText.getText().toString().trim();

            if (!text.equals("")) {
                return text;
            }
        }
        return null;
    }
}