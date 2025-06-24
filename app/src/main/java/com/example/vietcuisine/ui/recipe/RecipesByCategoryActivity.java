package com.example.vietcuisine.ui.recipe;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Recipe;
import java.util.ArrayList;
import java.util.List;

public class RecipesByCategoryActivity extends AppCompatActivity {
    
    private ImageButton backButton;
    private TextView categoryTitle;
    private RecyclerView recipesRecyclerView;
    private List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_by_category);
        
        initViews();
        setupClickListeners();
        setupRecyclerView();
        loadRecipes();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        categoryTitle = findViewById(R.id.categoryTitle);
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        recipesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // TODO: Set adapter when RecipeGridAdapter is created
    }

    private void loadRecipes() {
        String categoryId = getIntent().getStringExtra("category_id");
        String categoryName = getIntent().getStringExtra("category_name");
        
        if (categoryName != null) {
            categoryTitle.setText(categoryName);
        }
        
        // TODO: Load recipes by category from API
    }
}
