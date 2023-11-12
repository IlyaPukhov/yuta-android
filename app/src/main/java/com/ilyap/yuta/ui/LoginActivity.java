package com.ilyap.yuta.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ilyap.yuta.MainActivity;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.http.AuthResponse;
import com.ilyap.yuta.utils.JsonUtils;

public class LoginActivity extends AppCompatActivity {
    LoadingDialog loadingDialog;
    TextView errorText;
    private SharedPreferences sharedPreferences;
    private int user_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadingDialog = new LoadingDialog(LoginActivity.this);

        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("user_id", -1) >= 0) {
            openApp();
        }

        errorText = findViewById(R.id.error_text);
        Button buttonLogin = findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(v -> verifyLogin());
    }

    private void verifyLogin() {
        errorText.setVisibility(View.INVISIBLE);
        String login = getTextFromField(R.id.login);
        String password = getTextFromField(R.id.password);

        boolean successAuth = djangoRequest(login, password);
        if (successAuth) {
            saveUserId();
            openApp();
        } else {
            errorText.setVisibility(View.VISIBLE);
        }
    }

    private boolean djangoRequest(String login, String password) {
        loadingDialog.startLoadingDialog();
        // TODO  client.sendAsync()
        new Handler().postDelayed(() -> loadingDialog.dismissDialog(), 3000);

        String json = "{\"status\": \"OK\", \"user_id\": 1}";
        AuthResponse response = JsonUtils.parse(json, AuthResponse.class);

        if (response.getStatus().equalsIgnoreCase("ok")) {
            user_id = response.getUserId();
            return true;
        }
        return false;
    }

    private void openApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", user_id).apply();
    }

    private String getTextFromField(int fieldId) {
        return ((EditText) findViewById(fieldId)).getText().toString();
    }
}