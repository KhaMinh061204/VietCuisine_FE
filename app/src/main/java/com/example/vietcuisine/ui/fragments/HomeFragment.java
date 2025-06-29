package com.example.vietcuisine.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.LikeRequest;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.Post;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.ui.adapters.PostAdapter;
import com.example.vietcuisine.ui.messages.MessageListActivity;
import com.example.vietcuisine.ui.posts.PostDetailActivity;
import com.example.vietcuisine.ui.comments.CommentsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements PostAdapter.OnPostInteractionListener {

    private static final String TAG = "HomeFragment";

    private RecyclerView postsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PostAdapter postAdapter;
    private ApiService apiService;
    private List<Post> posts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        initViews(view);
        setupRecyclerViews();
        setupClickListeners();
        
        apiService = ApiClient.getClient().create(ApiService.class);
        
        loadData();
        
        return view;
    }

    private void initViews(View view) {
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        View headerView = view.findViewById(R.id.headerView);
        ImageButton messageButton = headerView.findViewById(R.id.messageButton);

        messageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MessageListActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerViews() {
        // Posts - Vertical
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(posts, this);
        postsRecyclerView.setAdapter(postAdapter);
    }

    private void setupClickListeners() {
        swipeRefreshLayout.setOnRefreshListener(this::loadData);
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        loadPosts();
    }

    private void loadPosts() {
        // Backend returns posts array directly 
        apiService.getAllPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    posts.clear();
                    posts.addAll(response.body());
                    postAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Posts loaded: " + posts.size());
                } else {
                    showError("Lỗi tải bài viết: " + response.code());
                    Log.e(TAG, "Posts request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showError("Lỗi tải bài viết: " + t.getMessage());
                Log.e(TAG, "Posts request failed", t);
            }
        });
    }

    @Override
    public void onPostClick(Post post) {
        // Navigate to post details
        Intent intent = new Intent(getContext(), PostDetailActivity.class);
        intent.putExtra("post_id", post.getId());
        startActivity(intent);
    }

    @Override
    public void onLikeClick(Post post) {
        // Handle like action
        apiService.toggleLike(new LikeRequest(post.getId(), "posts")).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    // Update UI
                    loadPosts();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                showError("Lỗi thao tác like");
            }
        });
    }

    @Override
    public void onCommentClick(Post post) {
        // Navigate to comments
        Intent intent = new Intent(getContext(), CommentsActivity.class);
        intent.putExtra("target_id", post.getId());
        intent.putExtra("target_type", "posts");
        startActivity(intent);
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
