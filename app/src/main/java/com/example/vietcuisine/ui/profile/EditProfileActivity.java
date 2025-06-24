package com.example.vietcuisine.ui.profile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vietcuisine.R;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {
    
    private ImageButton backButton;
    private TextInputEditText nameInput, phoneInput, emailInput;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        
        initViews();
        setupClickListeners();
        loadUserData();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);
        saveButton = findViewById(R.id.saveButton);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void loadUserData() {
        // TODO: Load current user data
    }

    private void saveProfile() {
        // TODO: Save profile changes
    }
}
