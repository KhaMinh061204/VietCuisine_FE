package com.example.vietcuisine.data.network;

import com.example.vietcuisine.utils.SharedPrefsManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        
        // Get token from SharedPrefsManager
        String token = SharedPrefsManager.getInstance().getAccessToken();          // Add Authorization header if token exists
        if (token != null && !token.isEmpty()) {
            Request authorizedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(authorizedRequest);
        }
        
        return chain.proceed(originalRequest);
    }
}
