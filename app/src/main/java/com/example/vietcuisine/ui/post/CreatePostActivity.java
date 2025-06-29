package com.example.vietcuisine.ui.post;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
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

public class CreatePostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView postImageView;
    private LinearLayout addPostImageContainer;
    private TextInputEditText captionInput;
    private Button submitPostButton;
    private ProgressBar progressBar;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        postImageView = findViewById(R.id.postImageView);
        addPostImageContainer = findViewById(R.id.addPostImageContainer);
        captionInput = findViewById(R.id.captionInput);
        submitPostButton = findViewById(R.id.submitPostButton);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        View.OnClickListener pickImageListener = v -> openImagePicker();
        postImageView.setOnClickListener(pickImageListener);
        addPostImageContainer.setOnClickListener(pickImageListener);

        submitPostButton.setOnClickListener(v -> submitPost());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            postImageView.setImageURI(imageUri);
            addPostImageContainer.setVisibility(View.GONE);
        }
    }

    private void submitPost() {
        String caption = captionInput.getText() != null ? captionInput.getText().toString().trim() : "";
        if (caption.isEmpty()) {
            captionInput.setError("Vui lòng nhập mô tả");
            return;
        }
        if (imageUri == null) {
            Toast.makeText(this, "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        submitPostButton.setEnabled(false);

        try {
            File imageFile = FileUtils.getFileFromUri(this, imageUri, ".jpg");
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), reqFile);

            RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), caption);

            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<ApiResponse> call = apiService.createPost(contentBody, imagePart);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    submitPostButton.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                        Toast.makeText(CreatePostActivity.this, "Đăng bài thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CreatePostActivity.this, "Đăng bài thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    submitPostButton.setEnabled(true);
                    Toast.makeText(CreatePostActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            progressBar.setVisibility(View.GONE);
            submitPostButton.setEnabled(true);
            Toast.makeText(this, "Không thể xử lý hình ảnh", Toast.LENGTH_SHORT).show();
        }
    }
}