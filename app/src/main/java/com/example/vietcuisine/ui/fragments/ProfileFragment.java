package com.example.vietcuisine.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.User;
import com.example.vietcuisine.data.model.UserResponse;
import com.example.vietcuisine.data.model.Recipe;
import com.example.vietcuisine.data.model.RecipeResponse;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.ui.adapters.ProfilePagerAdapter;
import com.example.vietcuisine.ui.adapters.ProfileRecipeAdapter;
import com.example.vietcuisine.ui.auth.LoginActivity;
import com.example.vietcuisine.ui.profile.EditProfileActivity;
import com.example.vietcuisine.ui.recipe.RecipeDetailActivity;
import com.google.android.material.tabs.TabLayout;
import com.example.vietcuisine.ui.profile.MyPosts;
import com.example.vietcuisine.ui.profile.MyRecipes;
import com.example.vietcuisine.ui.profile.MySavedRecipes;
import com.google.android.material.tabs.TabLayoutMediator;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView, menuButton;
    private TextView nameTextView, emailTextView, recipesCountTextView, followersCountTextView, followingCountTextView;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private ProfilePagerAdapter pagerAdapter;
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
        setupViewPager();
        setupClickListeners();
        
        apiService = ApiClient.getClient().create(ApiService.class);
        
        loadUserProfile();
        
        return view;
    }

    private void initViews(View view) {
        profileImageView = view.findViewById(R.id.profileImageView);
        menuButton = view.findViewById(R.id.menuButton);
        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        recipesCountTextView = view.findViewById(R.id.recipesCountTextView);
        followersCountTextView = view.findViewById(R.id.followersCountTextView);
//        followingCountTextView = view.findViewById(R.id.followingCountTextView);
        tabLayout = view.findViewById(R.id.profileTabLayout);
        viewPager = view.findViewById(R.id.profileViewPager);
    }

    private void setupViewPager() {
        pagerAdapter = new ProfilePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Công thức của tôi");
                    break;
                case 1:
                    tab.setText("Bài viết");
                    break;
                case 2:
                    tab.setText("Đã lưu");
                    break;
            }
        }).attach();
    }

    private void setupClickListeners() {
        menuButton.setOnClickListener(v -> showProfileMenu());
    }

    private void showProfileMenu() {
        PopupMenu popup = new PopupMenu(getContext(), menuButton);
        popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());

        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceShowIcon.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_edit_profile) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);

                // Nếu bạn muốn truyền dữ liệu người dùng qua
                intent.putExtra("name", currentUser.getName());
                intent.putExtra("email", currentUser.getEmail());
                intent.putExtra("phone", currentUser.getPhone());
                intent.putExtra("gender", currentUser.getGender());
                intent.putExtra("avatar", currentUser.getAvatar());

                startActivity(intent);
                return true;
            } else if (itemId == R.id.menu_settings) {
                showError("Chức năng cài đặt đang được phát triển");
                return true;
            } else if (itemId == R.id.menu_logout) {
                showLogoutDialog();
                return true;
            }
            return false;
        });
        
        popup.show();
    }

    private void loadUserProfile() {
        apiService.getUserProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {

                    currentUser = response.body();
                    updateUI();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showError("Lỗi tải thông tin người dùng: " + t.getMessage());
            }
        });
    }

//    private void loadUserRecipes() {
//        Call<RecipeResponse> call;
//        if (currentTab == 0) {
//            call = apiService.getMyRecipes();
//        } else {
//            call = apiService.getSavedRecipes();
//        }
//
//        call.enqueue(new Callback<RecipeResponse>() {
//            @Override
//            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    recipes.clear();
//                    recipes.addAll(response.body().getRecipes());
//                    recipeAdapter.notifyDataSetChanged();
//                    updateRecipesCount();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RecipeResponse> call, Throwable t) {
//                showError("Lỗi tải công thức: " + t.getMessage());
//            }
//        });
//    }

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
    }
}
