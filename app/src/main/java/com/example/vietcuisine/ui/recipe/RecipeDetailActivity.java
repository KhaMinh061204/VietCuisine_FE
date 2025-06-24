package com.example.vietcuisine.ui.recipe;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {
    
    private ImageButton backButton;
    private ImageView recipeImage;
    private TextView recipeTitle, recipeDescription, cookingTime, servings;
    private RecyclerView ingredientsRecyclerView, stepsRecyclerView;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        
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
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }

    private void setupRecyclerViews() {
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Set adapters when ingredient and step adapters are created
    }

    private void loadRecipeData() {
        String recipeId = getIntent().getStringExtra("recipe_id");
        // TODO: Load recipe data from API
        if (recipeId != null) {
            // Load recipe details
        }
    }
}
