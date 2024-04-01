package com.ilyap.yuta;

import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import lombok.Getter;
import lombok.Setter;

public class MainActivity extends AppCompatActivity {
    @Getter
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private int lastItemId = -1;
    private long lastPress = -1;
    @Setter
    private boolean isReadonlyProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onBackPressedDispatcherInitialize();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void onBackPressedDispatcherInitialize() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (lastItemId >= 0) {
                    selectPreviousNavTab();
                } else if (isReadonlyProfile) {
                    navController.popBackStack();
                    isReadonlyProfile = false;
                } else {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastPress > 5000L) {
                        Toast.makeText(getBaseContext(), getString(R.string.back_press), Toast.LENGTH_LONG)
                                .show();
                        lastPress = currentTime;
                    } else {
                        finish();
                    }
                }
            }
        });
    }

    public void selectNavTab(int id) {
        lastItemId = bottomNavigationView.getSelectedItemId();
        bottomNavigationView.setSelectedItemId(id);
    }

    private void selectPreviousNavTab() {
        bottomNavigationView.setSelectedItemId(lastItemId);
        lastItemId = -1;
    }
}