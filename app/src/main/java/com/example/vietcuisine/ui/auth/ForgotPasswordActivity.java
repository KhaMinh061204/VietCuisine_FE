package com.example.vietcuisine.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.ForgotPasswordRequest;
import com.example.vietcuisine.data.model.ApiResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout emailLayout;
    private TextInputEditText emailInput;
    private Button sendOtpButton;
    private ProgressBar progressBar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        
        initViews();
        setupClickListeners();
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    private void initViews() {
        emailLayout = findViewById(R.id.emailLayout);
        emailInput = findViewById(R.id.emailInput);
        sendOtpButton = findViewById(R.id.sendOtpButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        sendOtpButton.setOnClickListener(v -> sendOtp());
        findViewById(R.id.backToLoginText).setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void sendOtp() {
        String email = emailInput.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Email không hợp lệ");
            return;
        }

        emailLayout.setError(null);
        setLoading(true);

        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        
        apiService.forgotPassword(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "OTP đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, VerifyOtpActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Gửi OTP thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        sendOtpButton.setEnabled(!isLoading);
    }
}
