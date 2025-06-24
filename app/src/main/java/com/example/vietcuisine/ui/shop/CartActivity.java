package com.example.vietcuisine.ui.shop;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Ingredient;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    
    private ImageButton backButton;
    private RecyclerView cartRecyclerView;
    private List<Ingredient> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        initViews();
        setupClickListeners();
        setupRecyclerView();
        loadCartItems();
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
        // TODO: Set adapter when CartAdapter is created
    }

    private void loadCartItems() {
        // TODO: Load cart items from local storage or API
    }
}
