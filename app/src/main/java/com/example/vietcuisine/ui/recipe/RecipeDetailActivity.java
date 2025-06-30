package com.example.vietcuisine.ui.recipe;
import com.google.gson.Gson;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Recipe;
import com.example.vietcuisine.data.model.RecipeDetailResponse;
import com.example.vietcuisine.data.model.RecipeIngredient;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.ui.adapters.RecipeIngredientAdapter;
import com.example.vietcuisine.ui.adapters.RecipeStepAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {
    
    private static final String TAG = "RecipeDetailActivity";
    
    private ImageButton backButton;
    private ImageView recipeImage;
    private TextView recipeTitle, recipeDescription, cookingTime, servings;
    private TextView calories, protein, carbs, fat;
    private RecyclerView ingredientsRecyclerView, stepsRecyclerView;
      private RecipeIngredientAdapter ingredientAdapter;
    private RecipeStepAdapter stepAdapter;
    private ApiService apiService;
    
    private Recipe recipe;
    private String recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        
        apiService = ApiClient.getClient().create(ApiService.class);
        recipeId = getIntent().getStringExtra("recipe_id");
        Log.d(TAG, "Loading image from URL11: " + recipeId);
        initViews();
        setupClickListeners();
        setupRecyclerViews();
        loadRecipeData();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        recipeImage = findViewById(R.id.recipeImage);
        recipeTitle = findViewById(R.id.recipeTitle);
        recipeDescription = findViewById(R.id.recipeDescription);
        cookingTime = findViewById(R.id.cookingTime);
        servings = findViewById(R.id.servings);
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        stepsRecyclerView = findViewById(R.id.stepsRecyclerView);
        
        // Initialize nutrition TextViews if they exist in layout
        calories = findViewById(R.id.calories);
        protein = findViewById(R.id.protein);
        carbs = findViewById(R.id.carbs);
        fat = findViewById(R.id.fat);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }    private void setupRecyclerViews() {
        // Ingredients RecyclerView
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientAdapter = new RecipeIngredientAdapter(new ArrayList<>());
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
        
        // Steps RecyclerView
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepAdapter = new RecipeStepAdapter(new ArrayList<>());
        stepsRecyclerView.setAdapter(stepAdapter);
    }

    private void loadRecipeData() {
        if (recipeId == null || recipeId.isEmpty()) {
            showError("ID công thức không hợp lệ");
            finish();
            return;
        }

        Log.d(TAG, "Loading recipe with ID: " + recipeId);
        
        apiService.getRecipeById(recipeId).enqueue(new Callback<RecipeDetailResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailResponse> call, Response<RecipeDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecipeDetailResponse recipeResponse = response.body();
                    recipe = recipeResponse.getRecipe(); //

                    Log.d(TAG, "Response object 2: " + new Gson().toJson(recipeResponse));

                    if (recipeResponse.isSuccess() && recipe != null) {
                        displayRecipeData();
                        Log.d(TAG, "Recipe loaded successfully: " + recipe.getTitle());
                    } else {
                        showError("Không thể tải thông tin công thức");
                        Log.w(TAG, "Recipe data is null or unsuccessful");
                    }
                } else {
                    showError("Lỗi tải công thức:" + response.code());
                    Log.e(TAG, "Recipe request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RecipeDetailResponse> call, Throwable t) {
                showError("Lỗi kết nối: " + t.getMessage());
                Log.e(TAG, "Recipe request failed", t);
            }
        });
    }

    private void loadIngredientsFromApi(String recipeId) {
        apiService.getIngredientByRecipeId(recipeId).enqueue(new Callback<List<RecipeIngredient>>() {
            @Override
            public void onResponse(Call<List<RecipeIngredient>> call, Response<List<RecipeIngredient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RecipeIngredient> ingredients = response.body();
                    Log.d("RECIPE Ingredient API", "Tải thành công " + ingredients.size() + " nguyên liệu");
                    for (RecipeIngredient ing : ingredients) {
                        Log.d("RECIPE Ingredient API", "- " + ing.getName() + ": " + ing.getQuantity());
                    }

                    ingredientAdapter.updateIngredients(ingredients);
                } else {
                    Log.e("RECIPE Ingredient API", "Thất bại: " + response.code());
                    showError("Không tải được nguyên liệu");
                }
            }

            @Override
            public void onFailure(Call<List<RecipeIngredient>> call, Throwable t) {
                Log.e("RECIPE Ingredient API", "Lỗi kết nối", t);
                showError("Lỗi khi tải nguyên liệu");
            }
        });
    }

    private void displayRecipeData() {
        if (recipe == null) return;

        // Basic recipe information
        Log.d("RECIPE", "Tên món: " + recipe.getTitle());
        recipeTitle.setText(recipe.getTitle() != null ? recipe.getTitle() : "Không có tên");
        recipeDescription.setText(recipe.getDescription() != null ? recipe.getDescription() : "Không có mô tả");
        
        // Time and servings
        cookingTime.setText(recipe.getTime() > 0 ? recipe.getTime() + " phút" : "Không xác định");
        servings.setText("4 người"); // Default serving size
        
        // Load recipe image
        if (recipe.getImage() != null && !recipe.getImage().isEmpty()) {
            String imageUrl = ApiClient.getImageUrl(recipe.getImage());
            Log.d(TAG, "Loading image from URL: " + imageUrl);
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(recipeImage);
        }
        
        // Nutrition information (if TextViews exist)
        displayNutritionInfo();
        
        // Load ingredients
        loadIngredientsFromApi(recipeId);

        // Load steps
        if (recipe.getSteps() != null && !recipe.getSteps().isEmpty()) {
            stepAdapter.updateSteps(recipe.getSteps());
        }
    }
    
    private void displayNutritionInfo() {
        if (calories != null) {
            calories.setText(String.format("%.0fg", recipe.getCalories()));
        }
        if (protein != null) {
            protein.setText(String.format("%.0fg", recipe.getProtein()));
        }
        if (carbs != null) {
            carbs.setText(String.format("%.0fg", recipe.getCarbs()));
        }
        if (fat != null) {
            fat.setText(String.format("%.0fg", recipe.getFat()));
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
