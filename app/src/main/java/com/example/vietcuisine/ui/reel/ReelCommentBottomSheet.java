package com.example.vietcuisine.ui.reel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.data.model.Comment;
import com.example.vietcuisine.data.model.CommentRequest;
import com.example.vietcuisine.data.model.CommentResponse;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.ui.adapters.CommentAdapter;
import com.example.vietcuisine.utils.SharedPrefsManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReelCommentBottomSheet extends BottomSheetDialog {

    private final Context context;
    private final String reelId;
    private CommentAdapter adapter;

    public ReelCommentBottomSheet(@NonNull Context context, String reelId) {
        super(context);
        this.context = context;
        this.reelId = reelId;
        setupDialog();
    }

    private void setupDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reel_comment, null);
        setContentView(view);

        RecyclerView commentsRecycler = view.findViewById(R.id.commentsRecyclerView);
        EditText input = view.findViewById(R.id.inputComment);
        ImageButton sendButton = view.findViewById(R.id.sendCommentButton);

        String currentUserId = SharedPrefsManager.getInstance().getUserId();

        // Khởi tạo adapter với danh sách rỗng ban đầu
        List<Comment> commentList = new ArrayList<>();
        adapter = new CommentAdapter(context, commentList, currentUserId, null);
        commentsRecycler.setLayoutManager(new LinearLayoutManager(context));
        commentsRecycler.setAdapter(adapter);

        // Load bình luận
        loadComments();

        // Gửi bình luận khi bấm nút
        sendButton.setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (!text.isEmpty()) {

                createComment(text, input);
            }
        });
    }

    private void loadComments() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getComments(reelId, "reels").enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.getComments().clear();
                    adapter.getComments().addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Không có bình luận nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(context, "Không tải được bình luận", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void createComment(String content, EditText input) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        CommentRequest req = new CommentRequest(content, reelId, "reels", null);
        api.createComment(req).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("RESPONSE", "Code: " + response.code());
                Log.d("RESPONSE", "Message: " + response.message());
                Log.d("RESPONSE", "Body: " + new Gson().toJson(response.body()));

                if (response.body() != null && response.isSuccessful()) {
                    input.setText("");
                    loadComments(); // Reload bình luận
                } else {
                    Toast.makeText(context, "Gửi bình luận thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, "Không thể gửi bình luận", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
