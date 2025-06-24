package com.example.vietcuisine.ui.reel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vietcuisine.R;
import com.google.android.material.textfield.TextInputEditText;

public class CreateReelActivity extends AppCompatActivity {
    
    private static final int PICK_VIDEO_REQUEST = 1;
    
    private ImageButton backButton;
    private TextInputEditText captionInput;
    private Button selectVideoButton, uploadReelButton;
    private VideoView videoPreview;
    private TextView selectVideoPlaceholder;
    private Uri selectedVideoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reel);
        
        initViews();
        setupClickListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        captionInput = findViewById(R.id.captionInput);
        selectVideoButton = findViewById(R.id.selectVideoButton);
        uploadReelButton = findViewById(R.id.uploadReelButton);
        videoPreview = findViewById(R.id.videoPreview);
        selectVideoPlaceholder = findViewById(R.id.selectVideoPlaceholder);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        selectVideoButton.setOnClickListener(v -> selectVideo());
        uploadReelButton.setOnClickListener(v -> uploadReel());
        
        videoPreview.setOnClickListener(v -> selectVideo());
    }

    private void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedVideoUri = data.getData();
            if (selectedVideoUri != null) {
                selectVideoPlaceholder.setVisibility(TextView.GONE);
                videoPreview.setVideoURI(selectedVideoUri);
                videoPreview.start();
            }
        }
    }

    private void uploadReel() {
        String caption = captionInput.getText().toString().trim();
        
        if (selectedVideoUri == null) {
            selectVideoButton.setError("Vui lòng chọn video");
            return;
        }
        
        // TODO: Implement reel upload to server
        // For now, just show success message
        finish();
    }
}
