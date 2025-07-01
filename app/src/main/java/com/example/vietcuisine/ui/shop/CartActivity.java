package com.example.vietcuisine.ui.shop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.RecipeIngredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    
    private ImageButton backButton;
    private RecyclerView cartRecyclerView;
    private TextView tvTotal;
    private List<RecipeIngredient> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        initViews();
        setupClickListeners();
        tvTotal = findViewById(R.id.tvTotal);
        setupRecyclerView();
        loadCartItems();
        updateTotalPrice();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItems, this::updateTotalPrice);
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void loadCartItems() {
        SharedPreferences prefs = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        String cartJson = prefs.getString("cart_items", null);
        if (cartJson != null) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<List<RecipeIngredient>>(){}.getType();
            List<RecipeIngredient> loaded = gson.fromJson(cartJson, type);
            cartItems.clear();
            if (loaded != null) {
                for (RecipeIngredient item : loaded) {
                    item.setSelected(false); // Khi vào giỏ hàng, chưa tích nguyên liệu nào
                }
                cartItems.addAll(loaded);
            }
        }
        if (cartAdapter != null) cartAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCartItems();
    }

    private void saveCartItems() {
        SharedPreferences prefs = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        Gson gson = new Gson();
        prefs.edit().putString("cart_items", gson.toJson(cartItems)).apply();
    }

    private void updateTotalPrice() {
        double total = 0;
        for (RecipeIngredient item : cartItems) {
            if (!item.isSelected()) continue;
            int quantity = 1;
            String unit = "";
            try {
                String quantityStr = item.getQuantity();
                java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d+)(.*)").matcher(quantityStr.trim());
                if (matcher.find()) {
                    quantity = Integer.parseInt(matcher.group(1));
                    unit = matcher.group(2).trim().toLowerCase();
                }
            } catch (Exception ignored) {}
            double unitPrice = item.getUnitPrice();
            double amount = quantity;
            // Nếu đơn vị là gr thì đổi sang kg
            if (unit.equals("gr") || unit.equals("g")) {
                amount = quantity / 1000.0;
            }
            total += unitPrice * amount;
        }
        if (tvTotal != null) tvTotal.setText(String.format("Tổng: %.0f VND", total));
    }
}
