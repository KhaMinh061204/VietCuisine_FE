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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.Recipe;
import com.example.vietcuisine.data.model.Post;
import com.example.vietcuisine.data.model.Category;
import com.example.vietcuisine.data.model.RecipeResponse;
import com.example.vietcuisine.data.model.PostResponse;
import com.example.vietcuisine.data.model.CategoryResponse;
import com.example.vietcuisine.data.model.LikeRequest;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.ui.adapters.RecipeAdapter;
import com.example.vietcuisine.ui.adapters.PostAdapter;
import com.example.vietcuisine.ui.adapters.CategoryAdapter;
import com.example.vietcuisine.ui.recipe.CreateRecipeActivity;
import com.example.vietcuisine.ui.recipe.RecipeDetailActivity;
import com.example.vietcuisine.ui.recipe.RecipesByCategoryActivity;
import com.example.vietcuisine.ui.posts.PostDetailActivity;
import com.example.vietcuisine.ui.comments.CommentsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements PostAdapter.OnPostInteractionListener {

    private RecyclerView categoriesRecyclerView, featuredRecipesRecyclerView, postsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabAddPost;
    
    private CategoryAdapter categoryAdapter;
    private RecipeAdapter recipeAdapter;
    private PostAdapter postAdapter;
    
    private ApiService apiService;
    private List<Category> categories = new ArrayList<>();
    private List<Recipe> featuredRecipes = new ArrayList<>();
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
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        featuredRecipesRecyclerView = view.findViewById(R.id.featuredRecipesRecyclerView);
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        fabAddPost = view.findViewById(R.id.fabAddPost);
    }

    private void setupRecyclerViews() {
        // Categories - Horizontal
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(categories, this::onCategoryClick);
        categoriesRecyclerView.setAdapter(categoryAdapter);

        // Featured Recipes - Horizontal
        featuredRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recipeAdapter = new RecipeAdapter(featuredRecipes, this::onRecipeClick);
        featuredRecipesRecyclerView.setAdapter(recipeAdapter);

        // Posts - Vertical
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(posts, this);
        postsRecyclerView.setAdapter(postAdapter);
    }

    private void setupClickListeners() {
        swipeRefreshLayout.setOnRefreshListener(this::loadData);
        
        fabAddPost.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateRecipeActivity.class);
            startActivity(intent);
        });
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        loadCategories();
        loadFeaturedRecipes();
        loadPosts();
    }    private void loadCategories() {
        apiService.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    categories.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                showError("Lỗi tải danh mục: " + t.getMessage());
            }
        });
    }

    private void loadFeaturedRecipes() {
        apiService.getRecipesInHomepage().enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    featuredRecipes.clear();
                    featuredRecipes.addAll(response.body().getRecipes());
                    recipeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                showError("Lỗi tải công thức: " + t.getMessage());
            }
        });
    }

    private void loadPosts() {
        apiService.getAllPosts().enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    posts.clear();
                    posts.addAll(response.body().getPosts());
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showError("Lỗi tải bài viết: " + t.getMessage());
            }
        });
    }

    private void onCategoryClick(Category category) {
        // Navigate to recipes by category
        Intent intent = new Intent(getContext(), RecipesByCategoryActivity.class);
        intent.putExtra("category_id", category.getId());
        intent.putExtra("category_name", category.getName());
        startActivity(intent);
    }

    private void onRecipeClick(Recipe recipe) {
        // Navigate to recipe details
        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
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
