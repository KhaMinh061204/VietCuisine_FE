package com.example.vietcuisine.ui.reel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.utils.FileUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        if (caption.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập caption", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedVideoUri == null) {
            Toast.makeText(this, "Vui lòng chọn video", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy token từ SharedPreferences
        String token = getSharedPreferences("user_session", MODE_PRIVATE)
                .getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo phần caption
        RequestBody captionPart = RequestBody.create(
                caption, MediaType.parse("text/plain")
        );

        // Đọc video file
        File videoFile;
        try {
            videoFile = FileUtils.getFileFromUri(this, selectedVideoUri, ".mp4");

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi xử lý video: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo MultipartBody cho video
        RequestBody requestFile = RequestBody.create(videoFile, MediaType.parse("video/mp4"));
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFile);

        // Gọi API
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.addReel("Bearer " + token, captionPart, videoPart)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(CreateReelActivity.this, "Đăng video thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CreateReelActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(CreateReelActivity.this, "Thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
