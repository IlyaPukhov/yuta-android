package com.ilyap.yuta.ui;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;
import static com.ilyap.yuta.utils.UserUtils.setUserId;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ilyap.yuta.MainActivity;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.AuthResponse;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.LoadingDialog;
import com.ilyap.yuta.ui.dialogs.NetworkDialog;
import com.ilyap.yuta.utils.RequestViewModel;

public class LoginActivity extends AppCompatActivity {
    private TextView errorText;
    private RequestViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(RequestViewModel.class);

        if (hasInternetConnection() && getUserId(this) >= 0) {
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

        CustomDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.start();

        viewModel.getResultLiveData().removeObservers(this);
        viewModel.auth(login, password);
        viewModel.getResultLiveData().observe(this, result -> {
            if (!(result instanceof AuthResponse)) return;
            AuthResponse response = (AuthResponse) result;
            if (response.getStatus().equalsIgnoreCase("ok")) {
                setUserId(this, response.getUserId());
                openApp();
            } else {
                errorText.setVisibility(VISIBLE);
            }
            loadingDialog.dismiss();
        });
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

    private boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && (activeNetwork.getType() == TYPE_WIFI || activeNetwork.getType() == TYPE_MOBILE);
    }

    private String getTextFromField(int fieldId) {
        return ((EditText) findViewById(fieldId)).getText().toString();
    }
}