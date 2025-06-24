package com.example.vietcuisine.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.Reel;
import com.example.vietcuisine.data.model.ReelResponse;
import com.example.vietcuisine.data.model.LikeRequest;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.ui.adapters.ReelAdapter;
import com.example.vietcuisine.ui.reel.CreateReelActivity;
import com.example.vietcuisine.ui.comments.CommentsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReelsFragment extends Fragment implements ReelAdapter.OnReelInteractionListener {

    private ViewPager2 reelsViewPager;
    private FloatingActionButton fabAddReel;
    
    private ReelAdapter reelAdapter;
    private ApiService apiService;
    private List<Reel> reels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reels, container, false);
        
        initViews(view);
        setupViewPager();
        setupClickListeners();
        
        apiService = ApiClient.getClient().create(ApiService.class);
        
        loadReels();
        
        return view;
    }

    private void initViews(View view) {
        reelsViewPager = view.findViewById(R.id.reelsViewPager);
        fabAddReel = view.findViewById(R.id.fabAddReel);
    }

    private void setupViewPager() {
        reelAdapter = new ReelAdapter(getContext(), reels, this);
        reelsViewPager.setAdapter(reelAdapter);
        reelsViewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
    }

    private void setupClickListeners() {
        fabAddReel.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateReelActivity.class);
            startActivity(intent);
        });
    }

    private void loadReels() {
        apiService.getAllReels().enqueue(new Callback<ReelResponse>() {
            @Override
            public void onResponse(Call<ReelResponse> call, Response<ReelResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reels.clear();
                    reels.addAll(response.body().getReels());
                    reelAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ReelResponse> call, Throwable t) {
                showError("Lỗi tải reels: " + t.getMessage());
            }
        });
    }    @Override
    public void onLikeClick(Reel reel) {
        apiService.toggleLike(new LikeRequest(reel.getId(), "reels")).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    // Update reel like status locally
                    reel.setLiked(!reel.isLiked());
                    reel.setLikesCount(reel.isLiked() ? reel.getLikesCount() + 1 : reel.getLikesCount() - 1);
                    reelAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                showError("Lỗi thao tác like");
            }
        });
    }

    @Override
    public void onCommentClick(Reel reel) {
        Intent intent = new Intent(getContext(), CommentsActivity.class);
        intent.putExtra("target_id", reel.getId());
        intent.putExtra("target_type", "reels");
        startActivity(intent);
    }

    @Override
    public void onShareClick(Reel reel) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Xem reel này: " + reel.getCaption());
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ reel"));
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReels();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Pause any playing videos
        if (reelAdapter != null) {
            reelAdapter.pauseCurrentVideo();
        }
    }
}
