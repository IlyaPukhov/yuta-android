package com.yuta.__old.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.yuta.app.MainActivity;
import com.yuta.__old.R;
import com.yuta.app.domain.model.response.AuthorizationResponse;
import com.yuta.__old.ui.dialogs.CustomDialog;
import com.yuta.__old.ui.dialogs.LoadingDialog;
import com.yuta.__old.ui.dialogs.NetworkDialog;
import com.yuta.app.network._RequestUtils;
import com.yuta.app.network.RequestViewModel;
import com.yuta.app.util.UserUtils;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.yuta.app.util.UserUtils.getUserId;
import static com.yuta.app.util.UserUtils.setUserId;

public class LoginActivity extends AppCompatActivity {

    private TextView errorText;
    private RequestViewModel viewModel;
    private Button loginButton;
    private EditText loginView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_YUTA_Common);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(RequestViewModel.class);

        if (hasInternetConnection() && getUserId(this) >= 0) {
            _RequestUtils.setRootUrl(UserUtils.getSharedPreferences(this).getString("ipv4", ""));
            openApp();
        }

        if (!hasInternetConnection()) {
            openNetworkDialog();
        }

        loginView = findViewById(R.id.login);
        passwordView = findViewById(R.id.password);
        setupEditView(loginView);
        setupEditView(passwordView);

        errorText = findViewById(R.id.error_text);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            hideKeyboard();
            verifyLogin();
        });
    }

    private void verifyLogin() {
        String login = loginView.getText().toString();
        String password = passwordView.getText().toString();

        // TODO
        SharedPreferences.Editor editor = UserUtils.getSharedPreferences(this).edit();
        editor.putString("ipv4", ((EditText) findViewById(R.id.ipv4)).getText().toString()).apply();
        _RequestUtils.setRootUrl(UserUtils.getSharedPreferences(this).getString("ipv4", ""));

        CustomDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.start();

        viewModel.getResultLiveData().removeObservers(this);
        viewModel.auth(login, password);
        viewModel.getResultLiveData().observe(this, result -> {
            if (!(result instanceof AuthorizationResponse)) return;
            AuthorizationResponse response = (AuthorizationResponse) result;
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
                updateLoginButtonState();
            }
        });
    }

    private void updateLoginButtonState() {
        String loginText = loginView.getText().toString().trim();
        String passwordText = passwordView.getText().toString().trim();
        loginButton.setEnabled(!loginText.isEmpty() && !passwordText.isEmpty());
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
}