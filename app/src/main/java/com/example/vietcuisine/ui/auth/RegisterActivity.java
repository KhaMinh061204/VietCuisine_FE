package com.example.vietcuisine.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.RegisterRequest;
import com.example.vietcuisine.data.model.AuthResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout nameLayout, emailLayout, passwordLayout, phoneLayout, genderLayout;
    private TextInputEditText nameInput, emailInput, passwordInput, phoneInput;
    private AutoCompleteTextView genderInput;
    private Button registerButton;
    private ProgressBar progressBar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        initViews();
        setupGenderDropdown();
        setupClickListeners();
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    private void initViews() {
        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        genderLayout = findViewById(R.id.genderLayout);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        phoneInput = findViewById(R.id.phoneInput);
        genderInput = findViewById(R.id.genderInput);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupGenderDropdown() {
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_dropdown_item_1line, genders);
        genderInput.setAdapter(adapter);
    }    private void setupClickListeners() {
        registerButton.setOnClickListener(v -> attemptRegister());
        
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        
        findViewById(R.id.loginText).setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegister() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String gender = genderInput.getText().toString().trim();

        if (!validateInput(name, email, password, phone, gender)) {
            return;
        }

        setLoading(true);
        RegisterRequest request = new RegisterRequest(name, email, password, gender, phone);
        
        apiService.register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }    private boolean validateInput(String name, String email, String password, String phone, String gender) {
        boolean isValid = true;

        // Name validation
        if (name.isEmpty()) {
            nameLayout.setError("Tên không được để trống");
            isValid = false;
        } else {
            nameLayout.setError(null);
        }

        // Email validation - enhanced to match backend
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (email.isEmpty() || !email.matches(emailRegex)) {
            emailLayout.setError("Email không hợp lệ");
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        // Password validation - enhanced to match backend requirements
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{6,}$";
        if (password.isEmpty()) {
            passwordLayout.setError("Mật khẩu không được để trống");
            isValid = false;
        } else if (!password.matches(passwordRegex)) {
            passwordLayout.setError("Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường và ký tự đặc biệt");
            isValid = false;
        } else {
            passwordLayout.setError(null);
        }

        // Phone validation - enhanced to match backend (10-11 digits)
        String phoneRegex = "^\\d{10,11}$";
        if (phone.isEmpty()) {
            phoneLayout.setError("Số điện thoại không được để trống");
            isValid = false;
        } else if (!phone.matches(phoneRegex)) {
            phoneLayout.setError("Số điện thoại phải có 10-11 chữ số");
            isValid = false;
        } else {
            phoneLayout.setError(null);
        }

        // Gender validation
        if (gender.isEmpty()) {
            genderLayout.setError("Vui lòng chọn giới tính");
            isValid = false;
        } else {
            genderLayout.setError(null);
        }

        return isValid;
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        registerButton.setEnabled(!isLoading);
    }
}
