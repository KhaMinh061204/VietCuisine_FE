package com.example.vietcuisine.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.data.model.LikeRequest;
import com.example.vietcuisine.data.model.Reel;
import com.example.vietcuisine.data.model.ReelResponse;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.ui.adapters.ReelAdapter;
import com.example.vietcuisine.ui.comments.CommentsActivity;
import com.example.vietcuisine.ui.reel.ReelCommentBottomSheet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReelsFragment extends Fragment implements ReelAdapter.OnReelInteractionListener {

    private ViewPager2 reelsViewPager;
    private ReelAdapter reelAdapter;
    private ApiService apiService;
    private final List<Reel> reels = new ArrayList<>();
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reels, container, false);
        reelsViewPager = view.findViewById(R.id.reelsViewPager);

        setupViewPager();
        apiService = ApiClient.getClient().create(ApiService.class);
        loadReels();

        return view;
    }

    private void setupViewPager() {
        reelAdapter = new ReelAdapter(getContext(), reels, this);
        reelsViewPager.setAdapter(reelAdapter);
        reelsViewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        reelsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private int previousPosition = -1;

            @Override
            public void onPageSelected(int position) {
                if (previousPosition != -1) {
                    reelAdapter.pauseVideoAt(previousPosition);
                }
                reelAdapter.playVideoAt(position);
                previousPosition = position;
            }
        });
    }

    private void loadReels() {
        apiService.getAllReels().enqueue(new Callback<ReelResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReelResponse> call, @NonNull Response<ReelResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reels.clear();
                    reels.addAll(response.body().getReels());
                    reelAdapter.notifyDataSetChanged();
                    if (!reels.isEmpty()) {
                        reelsViewPager.post(() -> reelAdapter.playVideoAt(0));
                    }
                } else {
                    showError("Không tải được dữ liệu.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReelResponse> call, @NonNull Throwable t) {
                showError("Lỗi tải reels: " + t.getMessage());
            }
        });
    }

    @Override
    public void onLikeClick(Reel reel, int pos) {
        apiService.toggleLike(new LikeRequest("reels",reel.getId())).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                Log.d("respose api like","response like"+response);
                if (response.isSuccessful()) {
                    int pos = reels.indexOf(reel);
                    Log.d("position like","position"+pos);
                    if (pos != -1) {
                        reel.setLiked(!reel.isLiked());
                        int currentLikes = reel.getLikesCount();
                        int newLikes = reel.isLiked() ? currentLikes + 1 : currentLikes - 1;
                        reel.setLikesCount(newLikes);
                        reelAdapter.updateLikeState(pos, reel.isLiked(), newLikes);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                showError("Lỗi thao tác like");
            }
        });
    }

    @Override
    public void onCommentClick(Reel reel, int pos) {
        ReelCommentBottomSheet bottomSheet = new ReelCommentBottomSheet(requireContext(), reel.getId());
        bottomSheet.show();
    }


    @Override
    public void onShareClick(Reel reel, int pos) {
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
    public void onPause() {
        super.onPause();
        int currentPos = reelsViewPager.getCurrentItem();
        reelAdapter.pauseVideoAt(currentPos);
    }

    @Override
    public void onResume() {
        super.onResume();
        int currentPos = reelsViewPager.getCurrentItem();
        reelAdapter.playVideoAt(currentPos);
    }
}
