package com.example.vietcuisine;

import android.app.Application;
import android.content.Context;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.utils.SharedPrefsManager;

public class VietCuisineApplication extends Application {
    
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        
        // Initialize API client
        ApiClient.init();
        
        // Initialize SharedPreferences
        SharedPrefsManager.init(this);
    }

    public static Context getAppContext() {
        return context;
    }
}
