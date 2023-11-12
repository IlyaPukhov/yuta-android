package com.ilyap.yuta;

import static com.ilyap.yuta.ui.LoginActivity.EMPTY;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        System.out.println("111");

//        SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
//        // TODO Gson
//        String userJson = sharedPreferences.getString("user", EMPTY);
    }
}