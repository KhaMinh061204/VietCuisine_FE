package com.example.vietcuisine.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vietcuisine.R;
import com.example.vietcuisine.ui.auth.LoginActivity;
import com.example.vietcuisine.ui.main.MainActivity;
import com.example.vietcuisine.utils.SharedPrefsManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Check if user is logged in after a short delay
        new android.os.Handler().postDelayed(this::checkLoginStatus, 2000);
    }
    
    private void checkLoginStatus() {
        boolean isLoggedIn = SharedPrefsManager.getInstance().isLoggedIn();
        Log.d("isslogin","login"+isLoggedIn);
        Intent intent;
        if (isLoggedIn) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        
        startActivity(intent);
        finish();
    }
}
