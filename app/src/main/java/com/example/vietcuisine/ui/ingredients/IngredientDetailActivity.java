package com.example.vietcuisine.ui.ingredients;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Ingredient;

public class IngredientDetailActivity extends AppCompatActivity {
    
    private ImageButton backButton;
    private ImageView ingredientImage;
    private TextView ingredientName, ingredientDescription, ingredientPrice;
    private Ingredient ingredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_detail);
        
        initViews();
        setupClickListeners();
        loadIngredientData();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        ingredientImage = findViewById(R.id.ingredientImage);
        ingredientName = findViewById(R.id.ingredientName);
        ingredientDescription = findViewById(R.id.ingredientDescription);
        ingredientPrice = findViewById(R.id.ingredientPrice);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }

    private void loadIngredientData() {
        String ingredientId = getIntent().getStringExtra("ingredient_id");
        // TODO: Load ingredient data from API
        if (ingredientId != null) {
            // Load ingredient details
        }
    }
}
