package com.example.vietcuisine.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.User;
import com.example.vietcuisine.data.model.UserResponse;
import com.example.vietcuisine.data.model.Recipe;
import com.example.vietcuisine.data.model.RecipeResponse;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.ui.adapters.ProfileRecipeAdapter;
import com.example.vietcuisine.ui.auth.LoginActivity;
import com.example.vietcuisine.ui.profile.EditProfileActivity;
import com.example.vietcuisine.ui.settings.SettingsActivity;
import com.example.vietcuisine.ui.messages.MessagesActivity;
import com.example.vietcuisine.ui.recipe.RecipeDetailActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private TextView nameTextView, emailTextView, recipesCountTextView, followersCountTextView, followingCountTextView;    private LinearLayout editProfileButton, settingsButton, messagesButton;
    private LinearLayout logoutButton;
    private TabLayout tabLayout;
    private RecyclerView recipesRecyclerView;
    
    private ProfileRecipeAdapter recipeAdapter;
    private ApiService apiService;
    private List<Recipe> recipes = new ArrayList<>();
    private User currentUser;
    
    private int currentTab = 0; // 0: My Recipes, 1: Saved Recipes

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupTabs();
        setupClickListeners();
        
        apiService = ApiClient.getClient().create(ApiService.class);
        
        loadUserProfile();
        loadUserRecipes();
        
        return view;
    }

    private void initViews(View view) {
        profileImageView = view.findViewById(R.id.profileImageView);
        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        recipesCountTextView = view.findViewById(R.id.recipesCountTextView);
        followersCountTextView = view.findViewById(R.id.followersCountTextView);
        followingCountTextView = view.findViewById(R.id.followingCountTextView);        editProfileButton = view.findViewById(R.id.editProfileLayout);
        settingsButton = view.findViewById(R.id.settingsLayout);
        messagesButton = view.findViewById(R.id.myRecipesLayout); // Using available layout
        logoutButton = view.findViewById(R.id.logoutLayout);
        tabLayout = view.findViewById(R.id.profileTabLayout);
        recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);
    }

    private void setupRecyclerView() {
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeAdapter = new ProfileRecipeAdapter(getContext(), recipes, this::onRecipeClick);
        recipesRecyclerView.setAdapter(recipeAdapter);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Công thức của tôi"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã lưu"));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                loadUserRecipes();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupClickListeners() {
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });
        
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });
        
        messagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MessagesActivity.class);
            startActivity(intent);
        });
        
        logoutButton.setOnClickListener(v -> showLogoutDialog());
    }

    private void loadUserProfile() {
        apiService.getUserProfile().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body().getUser();
                    updateUI();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                showError("Lỗi tải thông tin người dùng: " + t.getMessage());
            }
        });
    }

    private void loadUserRecipes() {
        Call<RecipeResponse> call;
        if (currentTab == 0) {
            call = apiService.getMyRecipes();
        } else {
            call = apiService.getSavedRecipes();
        }
        
        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipes.clear();
                    recipes.addAll(response.body().getRecipes());
                    recipeAdapter.notifyDataSetChanged();
                    updateRecipesCount();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                showError("Lỗi tải công thức: " + t.getMessage());
            }
        });
    }

    private void updateUI() {
        if (currentUser != null) {
            nameTextView.setText(currentUser.getName());
            emailTextView.setText(currentUser.getEmail());
            
            if (currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
                Glide.with(this)
                    .load(currentUser.getAvatar())
                    .circleCrop()
                    .placeholder(R.drawable.ic_person)
                    .into(profileImageView);
            }
        }
    }

    private void updateRecipesCount() {
        recipesCountTextView.setText(String.valueOf(recipes.size()));
    }

    private void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
    }

    private void showLogoutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất?")
            .setPositiveButton("Đăng xuất", (dialog, which) -> logout())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void logout() {
        // Clear user session
        SharedPreferences prefs = getActivity().getSharedPreferences("user_session", getActivity().MODE_PRIVATE);
        prefs.edit().clear().apply();
        
        // Call logout API
        apiService.logout().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                redirectToLogin();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                redirectToLogin(); // Logout locally even if API call fails
            }
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserProfile();
        loadUserRecipes();
    }
}
