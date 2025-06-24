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
import com.example.vietcuisine.data.model.VerifyOtpRequest;
import com.example.vietcuisine.data.model.ApiResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpActivity extends AppCompatActivity {

    private TextInputLayout otpLayout;
    private TextInputEditText otpInput;
    private Button verifyButton, resendButton;
    private ProgressBar progressBar;
    private ApiService apiService;
    private String email;    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        
        // Get email from intent
        email = getIntent().getStringExtra("email");
        
        initViews();
        setupClickListeners();
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    private void initViews() {
        otpLayout = findViewById(R.id.otpLayout);
        otpInput = findViewById(R.id.otpInput);
        verifyButton = findViewById(R.id.verifyButton);
        resendButton = findViewById(R.id.resendButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        verifyButton.setOnClickListener(v -> verifyOtp());
        resendButton.setOnClickListener(v -> resendOtp());
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void verifyOtp() {
        String otp = otpInput.getText().toString().trim();

        if (otp.isEmpty() || otp.length() != 6) {
            otpLayout.setError("Vui lòng nhập mã OTP 6 số");
            return;
        }        otpLayout.setError(null);
        setLoading(true);

        VerifyOtpRequest request = new VerifyOtpRequest(email, otp);
        
        apiService.verifyOtp(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(VerifyOtpActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                    // Navigate to reset password activity or login
                    Intent intent = new Intent(VerifyOtpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(VerifyOtpActivity.this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(VerifyOtpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendOtp() {
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);
        // Implement resend OTP logic here
        // This would typically call the same API as forgot password to resend OTP
        Toast.makeText(this, "Đã gửi lại mã OTP", Toast.LENGTH_SHORT).show();
        setLoading(false);
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        verifyButton.setEnabled(!isLoading);
        resendButton.setEnabled(!isLoading);
    }
}
