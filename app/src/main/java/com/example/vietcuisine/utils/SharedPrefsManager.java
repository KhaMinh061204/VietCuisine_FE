package com.example.vietcuisine.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {

    private static final String PREF_NAME = "vietcuisine_prefs";
    private static SharedPrefsManager instance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    // Keys
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_GENDER = "user_gender";
    private static final String KEY_USER_AVATAR = "user_avatar";

    private SharedPrefsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Call once in Application class or Activity
    public static void init(Context context) {
        if (instance == null) {
            instance = new SharedPrefsManager(context.getApplicationContext());
        }
    }

    public static SharedPrefsManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SharedPrefsManager chưa được khởi tạo. Gọi init(context) trước!");
        }
        return instance;
    }

    // ==================== Token ====================
    public void setAccessToken(String token) {
        editor.putString(KEY_ACCESS_TOKEN, token).apply();
    }

    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    // ==================== Login Status ====================
    public void setLoggedIn(boolean loggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // ==================== User ID ====================
    public void saveUserId(String userId) {
        editor.putString(KEY_USER_ID, userId).apply();
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    // ==================== User Name ====================
    public void saveUserName(String name) {
        editor.putString(KEY_USER_NAME, name).apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    // ==================== User Email ====================
    public void saveUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email).apply();
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    // ==================== User Phone ====================
    public void saveUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone).apply();
    }

    public String getUserPhone() {
        return prefs.getString(KEY_USER_PHONE, null);
    }

    // ==================== User Gender ====================
    public void saveUserGender(String gender) {
        editor.putString(KEY_USER_GENDER, gender).apply();
    }

    public String getUserGender() {
        return prefs.getString(KEY_USER_GENDER, null);
    }

    // ==================== User Avatar ====================
    public void saveUserAvatar(String avatarUrl) {
        editor.putString(KEY_USER_AVATAR, avatarUrl).apply();
    }

    public String getUserAvatar() {
        return prefs.getString(KEY_USER_AVATAR, null);
    }

    // ==================== Clear All ====================
    public void clearAll() {
        editor.clear().apply();
    }
}
