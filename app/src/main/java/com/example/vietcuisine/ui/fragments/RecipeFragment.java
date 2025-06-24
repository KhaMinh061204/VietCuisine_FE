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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.Recipe;
import com.example.vietcuisine.data.model.RecipeResponse;
import com.example.vietcuisine.ui.adapters.RecipeGridAdapter;
import com.example.vietcuisine.ui.recipe.CreateRecipeActivity;
import com.example.vietcuisine.ui.recipe.RecipeDetailActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment {

    private TabLayout tabLayout;
    private RecyclerView recipesRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabAddRecipe;
    
    private RecipeGridAdapter recipeAdapter;
    private ApiService apiService;
    private List<Recipe> recipes = new ArrayList<>();
    
    private int currentTab = 0; // 0: All, 1: My Recipes, 2: Saved

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupTabs();
        setupClickListeners();
        
        apiService = ApiClient.getClient().create(ApiService.class);
        
        loadRecipes();
        
        return view;
    }

    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        fabAddRecipe = view.findViewById(R.id.fabAddRecipe);
    }

    private void setupRecyclerView() {
        recipesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recipeAdapter = new RecipeGridAdapter(recipes, this::onRecipeClick);
        recipesRecyclerView.setAdapter(recipeAdapter);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Tất cả"));
        tabLayout.addTab(tabLayout.newTab().setText("Của tôi"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã lưu"));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                loadRecipes();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupClickListeners() {
        swipeRefreshLayout.setOnRefreshListener(this::loadRecipes);
        
        fabAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateRecipeActivity.class);
            startActivity(intent);
        });
    }

    private void loadRecipes() {
        swipeRefreshLayout.setRefreshing(true);
        
        Call<RecipeResponse> call;
        switch (currentTab) {
            case 1: // My Recipes
                call = apiService.getMyRecipes();
                break;
            case 2: // Saved Recipes
                call = apiService.getSavedRecipes();
                break;
            default: // All Recipes
                call = apiService.getAllRecipes();
                break;
        }
        
        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    recipes.clear();
                    recipes.addAll(response.body().getRecipes());
                    recipeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showError("Lỗi tải công thức: " + t.getMessage());
            }
        });
    }

    private void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecipes(); // Refresh when returning to fragment
    }
}
