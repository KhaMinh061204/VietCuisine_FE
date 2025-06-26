package com.example.vietcuisine.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.Recipe;
import com.example.vietcuisine.data.model.RecipeResponse;
import com.example.vietcuisine.data.model.Category;
import com.example.vietcuisine.data.model.CategoryResponse;
import com.example.vietcuisine.ui.adapters.CategoryAdapter;
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

    private static final String TAG = "RecipeFragment";
    private SearchView searchView;
    private TabLayout tabLayout;
    private RecyclerView recipesRecyclerView, categoriesRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabAddRecipe;
    
    private RecipeGridAdapter recipeAdapter;
    private CategoryAdapter categoryAdapter;
    private ApiService apiService;
    private List<Recipe> recipes = new ArrayList<>();
    private List<Recipe> filteredRecipes = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    
    private int currentTab = 0; // 0: All, 1: My Recipes, 2: Saved

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupCategoriesRecyclerView();
        setupTabs();
        setupClickListeners();
        setupSearch();
        
        apiService = ApiClient.getClient().create(ApiService.class);
        
        loadRecipes();
        loadCategories();
        
        return view;
    }

    private void initViews(View view) {
        searchView = view.findViewById(R.id.searchView);
        tabLayout = view.findViewById(R.id.tabLayout);
        recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        fabAddRecipe = view.findViewById(R.id.fabAddRecipe);
    }

    private void setupRecyclerView() {
        recipesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recipeAdapter = new RecipeGridAdapter(filteredRecipes, this::onRecipeClick);
        recipesRecyclerView.setAdapter(recipeAdapter);
    }

    private void setupCategoriesRecyclerView() {
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(categories, this::onCategoryClick);
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Tất cả"));
        tabLayout.addTab(tabLayout.newTab().setText("Công thức của tôi"));
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

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterRecipes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRecipes(newText);
                return true;
            }
        });
    }

    private void filterRecipes(String query) {
        filteredRecipes.clear();
        if (query.isEmpty()) {
            filteredRecipes.addAll(recipes);
        } else {
            for (Recipe recipe : recipes) {
                if (recipe.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    (recipe.getDescription() != null && recipe.getDescription().toLowerCase().contains(query.toLowerCase()))) {
                    filteredRecipes.add(recipe);
                }
            }
        }
        recipeAdapter.notifyDataSetChanged();
    }

    private void loadCategories() {
        // Backend returns { categories: [...] } directly, matching CategoryResponse structure
        apiService.getAllCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CategoryResponse categoryResponse = response.body();
                    if (categoryResponse.getCategories() != null && !categoryResponse.getCategories().isEmpty()) {
                        categories.clear();
                        categories.addAll(categoryResponse.getCategories());
                        categoryAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Categories loaded: " + categories.size());
                    } else {
                        showError("Không có danh mục nào");
                        Log.w(TAG, "No categories found in response");
                    }
                } else {
                    showError("Lỗi tải danh mục: " + response.code());
                    Log.e(TAG, "Categories request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                showError("Lỗi tải danh mục: " + t.getMessage());
                Log.e(TAG, "Categories request failed", t);
            }
        });
    }

    private void loadRecipes() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }

        Call<RecipeResponse> call;
        switch (currentTab) {
            case 0: // All recipes
                call = apiService.getAllRecipes();
                break;
            case 1: // My recipes
                call = apiService.getMyRecipes();
                break;
            case 2: // Saved recipes
                call = apiService.getSavedRecipes();
                break;
            default:
                call = apiService.getAllRecipes();
                break;
        }

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                if (response.isSuccessful() && response.body() != null) {
                    recipes.clear();
                    if (response.body().getRecipes() != null) {
                        recipes.addAll(response.body().getRecipes());
                    }
                    filteredRecipes.clear();
                    filteredRecipes.addAll(recipes);
                    recipeAdapter.notifyDataSetChanged();
                } else {
                    showError("Không thể tải danh sách công thức");
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
    }

    private void onCategoryClick(Category category) {
        // TODO: Handle category click event, e.g., navigate to a new screen
        Toast.makeText(getContext(), "Clicked on " + category.getName(), Toast.LENGTH_SHORT).show();
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecipes(); // Refresh recipes when returning to fragment
    }
}
