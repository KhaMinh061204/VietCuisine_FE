package com.example.vietcuisine.ui.shop;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.IngredientOrder;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    
    private ImageButton backButton;
    private RecyclerView ordersRecyclerView;
    private List<IngredientOrder> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        
        initViews();
        setupClickListeners();
        setupRecyclerView();
        loadOrderHistory();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Set adapter when OrderHistoryAdapter is created
    }

    private void loadOrderHistory() {
        // TODO: Load order history from API
    }
}
