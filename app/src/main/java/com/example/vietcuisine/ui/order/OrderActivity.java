package com.example.vietcuisine.ui.order;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
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
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Handle back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        tvTotal = findViewById(R.id.tvTotal);

        ArrayList<RecipeIngredient> selectedIngredients =
                (ArrayList<RecipeIngredient>) getIntent().getSerializableExtra("selected_ingredients");

        ingredientAdapter = new RecipeIngredientAdapter(selectedIngredients != null ? selectedIngredients : new ArrayList<>(), true, this::updateTotalPrice);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        updateTotalPrice();
        // TODO: Implement other order functionality (user info, payment, etc.)
    }

    private void updateTotalPrice() {
        double total = 0;
        if (ingredientAdapter != null) {
            for (RecipeIngredient ingredient : ingredientAdapter.getSelectedIngredients()) {
                // Parse quantity and unit
                String quantityStr = ingredient.getQuantity();
                int quantity = 1;
                java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d+)").matcher(quantityStr.trim());
                if (matcher.find()) {
                    try { quantity = Integer.parseInt(matcher.group(1)); } catch (Exception ignored) {}
                }
                // Use unitPrice from RecipeIngredient directly
                double unitPrice = ingredient.getUnitPrice();
                total += unitPrice * quantity;
            }
        }
        if (tvTotal != null) tvTotal.setText(String.format("Tổng: %.0f VND", total));
    }
}