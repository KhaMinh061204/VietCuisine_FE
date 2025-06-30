package com.example.vietcuisine.ui.order;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.RecipeIngredient;
import com.example.vietcuisine.ui.adapters.RecipeIngredientAdapter;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView ingredientsRecyclerView;
    private RecipeIngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Handle back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);

        ArrayList<RecipeIngredient> selectedIngredients =
                (ArrayList<RecipeIngredient>) getIntent().getSerializableExtra("selected_ingredients");

        ingredientAdapter = new RecipeIngredientAdapter(selectedIngredients != null ? selectedIngredients : new ArrayList<>());
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        // TODO: Implement other order functionality (user info, payment, etc.)
    }
}