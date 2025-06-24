package com.example.vietcuisine.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.Account;
import com.example.vietcuisine.data.model.AuthResponse;
import com.example.vietcuisine.data.model.LoginRequest;
import com.example.vietcuisine.data.model.User;
import com.example.vietcuisine.data.model.UserResponse;
import com.example.vietcuisine.ui.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailInput, passwordInput;
    private Button loginButton;
    private TextView registerLink, forgotPasswordLink;
    private ProgressBar progressBar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initViews();
        setupClickListeners();
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    private void initViews() {
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
        
        forgotPasswordLink.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        setLoading(true);
        LoginRequest request = new LoginRequest(email, password);
          apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setLoading(false);
                Log.d("LoginActivity", "Response code: " + response.code());
                  if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d("LoginActivity", "Token: " + authResponse.getToken());
                    Log.d("LoginActivity", "Message: " + authResponse.getMessage());
                    Log.d("LoginActivity", "User: " + (authResponse.getUser() != null ? "not null" : "null"));
                    Log.d("LoginActivity", "Account: " + (authResponse.getAccount() != null ? "not null" : "null"));
                    
                    saveUserSession(authResponse);
                    
                    // Fetch full user profile after login
                    fetchUserProfile();
                } else {
                    Log.e("LoginActivity", "Login failed. Response: " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            Log.e("LoginActivity", "Error body: " + response.errorBody().string());
                        }
                    } catch (Exception e) {
                        Log.e("LoginActivity", "Error reading error body: " + e.getMessage());
                    }
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String email, String password) {
        boolean isValid = true;

        if (email.isEmpty()) {
            emailLayout.setError("Email không được để trống");
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        if (password.isEmpty()) {
            passwordLayout.setError("Mật khẩu không được để trống");
            isValid = false;
        } else {
            passwordLayout.setError(null);
        }

        return isValid;
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!isLoading);
    }    private void saveUserSession(AuthResponse authResponse) {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        // Save token
        if (authResponse.getToken() != null) {
            editor.putString("token", authResponse.getToken());
        }
        
        // Try to get user information
        User user = authResponse.getUser();
        Account account = authResponse.getAccount();
        
        if (user != null) {
            // Full user object available
            editor.putString("user_id", user.getId());
            editor.putString("user_name", user.getName());
            editor.putString("user_email", user.getEmail());
        } else if (account != null) {
            // Only account info available
            editor.putString("account_id", account.getId());
            editor.putString("user_email", account.getEmail());
            editor.putString("user_role", account.getRole());
            
            // Save user ID if available
            String userId = account.getUserId();
            if (userId != null) {
                editor.putString("user_id", userId);
            }
            
            // If account has populated user object
            User accountUser = account.getUser();
            if (accountUser != null) {
                editor.putString("user_name", accountUser.getName());
                if (accountUser.getEmail() != null) {
                    editor.putString("user_email", accountUser.getEmail());
                }
            }
        }
        
        editor.putBoolean("is_logged_in", true);
        editor.apply();
        
        Log.d("LoginActivity", "User session saved successfully");
    }

    private void fetchUserProfile() {
        apiService.getUserProfile().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    if (userResponse.getUser() != null) {
                        // Update user session with full user info
                        updateUserSession(userResponse.getUser());
                        Log.d("LoginActivity", "User profile fetched successfully");
                    }
                } else {
                    Log.e("LoginActivity", "Failed to fetch user profile: " + response.message());
                }
                
                // Navigate to main regardless of profile fetch result
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("LoginActivity", "Error fetching user profile: " + t.getMessage());
                // Navigate to main even if profile fetch fails
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            }
        });
    }

    private void updateUserSession(User user) {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        if (user.getId() != null) {
            editor.putString("user_id", user.getId());
        }
        if (user.getName() != null) {
            editor.putString("user_name", user.getName());
        }
        if (user.getEmail() != null) {
            editor.putString("user_email", user.getEmail());
        }
        if (user.getPhone() != null) {
            editor.putString("user_phone", user.getPhone());
        }
        if (user.getGender() != null) {
            editor.putString("user_gender", user.getGender());
        }
        if (user.getAvatar() != null) {
            editor.putString("user_avatar", user.getAvatar());
        }
        
        editor.apply();
        Log.d("LoginActivity", "User session updated with full profile");
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
