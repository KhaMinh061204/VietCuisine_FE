package com.example.vietcuisine.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.Ingredient;
import com.example.vietcuisine.data.model.IngredientResponse;
import com.example.vietcuisine.data.model.IngredientOrder;
import com.example.vietcuisine.ui.adapters.IngredientAdapter;
import com.example.vietcuisine.ui.ingredients.IngredientDetailActivity;
import com.example.vietcuisine.ui.shop.CartActivity;
import com.example.vietcuisine.ui.shop.OrderHistoryActivity;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView ingredientsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabCart, fabOrders;
    
    private IngredientAdapter ingredientAdapter;
    private ApiService apiService;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Ingredient> cartItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupSearchView();
        setupClickListeners();
        
        apiService = ApiClient.getClient().create(ApiService.class);
        
        loadIngredients();
        updateCartBadge();
        
        return view;
    }

    private void initViews(View view) {
        searchView = view.findViewById(R.id.searchView);
        ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        fabCart = view.findViewById(R.id.fabCart);
        fabOrders = view.findViewById(R.id.fabOrders);
    }    private void setupRecyclerView() {
        ingredientsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ingredientAdapter = new IngredientAdapter(getContext(), ingredients, new IngredientAdapter.OnIngredientClickListener() {
            @Override
            public void onIngredientClick(Ingredient ingredient) {
                ShopFragment.this.onIngredientClick(ingredient);
            }

            @Override
            public void onAddToCartClick(Ingredient ingredient) {
                ShopFragment.this.onAddToCartClick(ingredient);
            }
        });
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchIngredients(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadIngredients();
                }
                return true;
            }
        });
    }

    private void setupClickListeners() {
        swipeRefreshLayout.setOnRefreshListener(this::loadIngredients);
        
        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CartActivity.class);
            startActivity(intent);
        });
        
        fabOrders.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
            startActivity(intent);
        });
    }

    private void loadIngredients() {
        swipeRefreshLayout.setRefreshing(true);
        
        apiService.getAllIngredients().enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    ingredients.clear();
                    ingredients.addAll(response.body().getIngredients());
                    ingredientAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showError("Lỗi tải nguyên liệu: " + t.getMessage());
            }
        });
    }

    private void searchIngredients(String query) {
        apiService.searchIngredients(query).enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ingredients.clear();
                    ingredients.addAll(response.body().getIngredients());
                    ingredientAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                showError("Lỗi tìm kiếm: " + t.getMessage());
            }
        });
    }

    private void onIngredientClick(Ingredient ingredient) {
        Intent intent = new Intent(getContext(), IngredientDetailActivity.class);
        intent.putExtra("ingredient_id", ingredient.getId());
        startActivity(intent);
    }

    private void onAddToCartClick(Ingredient ingredient) {
        // Add to local cart
        boolean found = false;
        for (Ingredient item : cartItems) {
            if (item.getId().equals(ingredient.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }
        
        if (!found) {
            Ingredient cartItem = new Ingredient(ingredient);
            cartItem.setQuantity(1);
            cartItems.add(cartItem);
        }
        
        // Save to SharedPreferences or local database
        saveCartToLocal();
        updateCartBadge();
        
        Toast.makeText(getContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    private void saveCartToLocal() {
        // Implementation to save cart items to SharedPreferences
        // This would typically use Gson to serialize the cart items
    }

    private void updateCartBadge() {
        if (cartItems.size() > 0) {
            BadgeDrawable badge = BadgeDrawable.create(getContext());
            badge.setNumber(cartItems.size());
            badge.setVisible(true);
            // Apply badge to cart FAB
        }
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadIngredients();
        updateCartBadge();
    }
}
