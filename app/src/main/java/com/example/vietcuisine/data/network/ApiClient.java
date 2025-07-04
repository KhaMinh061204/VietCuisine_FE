package com.example.vietcuisine.data.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:3001/"; // For Android emulator
    // Use "http://192.168.1.x:3001/" for real device (replace with your IP)
    
    private static Retrofit retrofit = null;

    /**
     * Initialize the API client during application startup
     */
    public static void init() {
        // Initialize the Retrofit client by calling getClient()
        getClient();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Create logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Create auth interceptor
            AuthInterceptor authInterceptor = new AuthInterceptor();

            // Create OkHttpClient
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(authInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Create Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Get the base URL for API requests
     * @return The base URL string
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * Helper method to construct proper image URLs
     * @param imagePath The image path from the API response
     * @return The complete image URL
     */
    public static String getImageUrl(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return "";
        }
        
        // If imagePath already starts with http, return as is
        if (imagePath.startsWith("http")) {
            return imagePath;
        }
        
        // If imagePath starts with /, remove BASE_URL trailing slash to avoid double slash
        if (imagePath.startsWith("/")) {
            return BASE_URL.replaceAll("/$", "") + imagePath;
        } else {
            return BASE_URL + imagePath;
        }
    }
}
