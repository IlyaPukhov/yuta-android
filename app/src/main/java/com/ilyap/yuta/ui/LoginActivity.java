package com.ilyap.yuta.ui;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ilyap.yuta.MainActivity;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.AuthResponse;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.LoadingDialog;
import com.ilyap.yuta.ui.dialogs.NetworkDialog;
import com.ilyap.yuta.utils.JsonUtils;
import com.ilyap.yuta.utils.RequestUtils;

public class LoginActivity extends AppCompatActivity {
    TextView errorText;
    private SharedPreferences sharedPreferences;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        if (hasInternetConnection() && sharedPreferences.getInt("user_id", -1) >= 0) {
            openApp();
        }

        if (!hasInternetConnection()) {
            openNetworkDialog();
        }

        errorText = findViewById(R.id.error_text);
        Button buttonLogin = findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(v -> verifyLogin());
    }

    private void verifyLogin() {
        errorText.setVisibility(GONE);
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
        CustomDialog loadingDialog = new LoadingDialog(LoginActivity.this);
        // TODO async
        loadingDialog.start();
        String json = RequestUtils.getUserIdRequest();
        loadingDialog.dismiss();

        AuthResponse response = JsonUtils.parse(json, AuthResponse.class);
        if (response.getStatus().equalsIgnoreCase("ok")) {
            user_id = response.getUserId();
            return true;
        }
        return false;
    }

    private void openApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void openNetworkDialog() {
        CustomDialog networkDialog = new NetworkDialog(LoginActivity.this);
        networkDialog.start();
    }

    private void saveUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", user_id).apply();
    }

    private boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && (activeNetwork.getType() == TYPE_WIFI || activeNetwork.getType() == TYPE_MOBILE);
    }

    private String getTextFromField(int fieldId) {
        return ((EditText) findViewById(fieldId)).getText().toString();
    }
}