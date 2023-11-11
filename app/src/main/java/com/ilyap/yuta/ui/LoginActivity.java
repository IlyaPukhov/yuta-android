package com.ilyap.yuta.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ilyap.yuta.MainActivity;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.teams.Team;
import com.ilyap.yuta.models.users.Direction;
import com.ilyap.yuta.models.users.Faculty;
import com.ilyap.yuta.models.users.Group;
import com.ilyap.yuta.models.users.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class LoginActivity extends AppCompatActivity {
    public static final String EMPTY = "";
    LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
    private SharedPreferences sharedPreferences;
    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);

        if (!sharedPreferences.getString("user", EMPTY).isEmpty()) {
            openApp();
        } else {
            Button buttonLogin = findViewById(R.id.login_button);
            buttonLogin.setOnClickListener(v -> verifyLogin());
        }
    }

    private void verifyLogin() {
        String login = ((EditText) findViewById(R.id.login)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        boolean successAuth = djangoRequest(login, password);

        if (successAuth) {
            saveAuthState();
            openApp();
        } else {
            findViewById(R.id.error_text).setVisibility(View.VISIBLE);
        }
    }

    private boolean djangoRequest(String login, String password) {
        loadingDialog.startLoadingDialog();
        // TODO  client.sendAsync()
        new Handler().postDelayed(() -> loadingDialog.dismissDialog(), 3000);

        user = new User(1, "photo", "cropped-photo", "puhovin.21", "Пухов", "Илья", "Николаевич", LocalDate.of(2003, 8, 1), "+7 (999) 740 24-33", "dinamond2003@gmail.com", "https://vk.com/ilya.pukhov", "Bio", new Faculty(1, "Цифровых Систем"), new Direction(1, "09", "Дирекция"), new Group(1, "Программная инженерия"), new ArrayList<Team>((Collection) new Team(1, "Робозавры", null, new ArrayList<User>((Collection) new User()))));
        return true;
    }

    private void openApp() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void saveAuthState() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // TODO Gson
        editor.putString("user", user.toString()).apply();
    }
}