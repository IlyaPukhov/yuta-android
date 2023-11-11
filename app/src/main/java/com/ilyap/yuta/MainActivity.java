package com.ilyap.yuta;

import static com.ilyap.yuta.ui.LoginActivity.EMPTY;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);

        // TODO Gson
        String userJson = sharedPreferences.getString("user", EMPTY);
    }
}