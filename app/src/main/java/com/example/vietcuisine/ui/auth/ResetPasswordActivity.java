package com.example.vietcuisine.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.data.model.ResetPasswordRequest;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout newPasswordLayout, confirmPasswordLayout;
    private TextInputEditText newPasswordInput, confirmPasswordInput;
    private MaterialButton resetPasswordButton;
    private ProgressBar progressBar;

    private ApiService apiService;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initViews();
        setupListeners();

        apiService = ApiClient.getClient().create(ApiService.class);

        email = getIntent().getStringExtra("email");
    }

    private void initViews() {
        newPasswordLayout = findViewById(R.id.newPasswordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        resetPasswordButton.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String newPassword = newPasswordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (newPassword.isEmpty() || newPassword.length() < 6) {
            newPasswordLayout.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        } else {
            newPasswordLayout.setError(null);
        }

        if (!newPassword.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Mật khẩu xác nhận không khớp");
            return;
        } else {
            confirmPasswordLayout.setError(null);
        }

        setLoading(true);

        ResetPasswordRequest request = new ResetPasswordRequest(email, newPassword);

        apiService.resetPassword(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Thất bại! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(ResetPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        resetPasswordButton.setEnabled(!isLoading);
    }
}
