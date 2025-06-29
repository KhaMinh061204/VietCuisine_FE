package com.example.vietcuisine.ui.profile;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.UpdateProfileRequest;
import com.example.vietcuisine.data.model.User;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    
    private ImageView backButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Nhận dữ liệu từ intent
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String gender = getIntent().getStringExtra("gender");
        String avatar = getIntent().getStringExtra("avatar");

        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
        // Gán vào các view
        TextInputEditText nameInput = findViewById(R.id.nameInput);
        TextInputEditText emailInput = findViewById(R.id.emailInput);
        TextInputEditText phoneInput = findViewById(R.id.phoneInput);
        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        // Tạo adapter từ mảng resource
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Gán adapter cho Spinner
        genderSpinner.setAdapter(adapter);
        ImageView avatarImageView = findViewById(R.id.avatarImageView);

        if (name != null) nameInput.setText(name);
        if (email != null) emailInput.setText(email);
        if (phone != null) phoneInput.setText(phone);

        // Set spinner gender
        if (gender != null) {
            String[] genders = getResources().getStringArray(R.array.gender_array); // ví dụ: ["Nam", "Nữ", "Khác"]
            for (int i = 0; i < genders.length; i++) {
                if (genders[i].equalsIgnoreCase(gender)) {
                    genderSpinner.setSelection(i);
                    break;
                }
            }
        }

        // Load avatar (nếu có)
        if (avatar != null && !avatar.isEmpty()) {
            Glide.with(this)
                    .load(avatar)
                    .circleCrop()
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .into(avatarImageView);
        }
        setupClickListeners();
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        TextInputEditText nameInput = findViewById(R.id.nameInput);
        TextInputEditText emailInput = findViewById(R.id.emailInput);
        TextInputEditText phoneInput = findViewById(R.id.phoneInput);
        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        ImageView avatarImageView = findViewById(R.id.avatarImageView);

        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();

        // Lấy lại avatar URL (nếu đã được truyền từ intent)
        String avatar = getIntent().getStringExtra("avatar");

        // Tạo request object
        UpdateProfileRequest request = new UpdateProfileRequest(name, email, gender, phone, avatar);

        // Gọi API
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<User> call = apiService.updateProfile(request);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // hoặc quay lại ProfileActivity
                } else {
                    Toast.makeText(EditProfileActivity.this, "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
