package com.example.vietcuisine.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.OrderListResponse;
import com.example.vietcuisine.data.model.RecipeResponse;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.Ingredient;
import com.example.vietcuisine.data.model.IngredientResponse;
import com.example.vietcuisine.data.model.IngredientOrder;
import com.example.vietcuisine.ui.adapters.IngredientAdapter;
import com.example.vietcuisine.ui.adapters.OrderAdapter;
import com.example.vietcuisine.ui.ingredients.IngredientDetailActivity;
import com.example.vietcuisine.ui.shop.CartActivity;
import com.example.vietcuisine.ui.shop.OrderHistoryActivity;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShopFragment extends Fragment {
    private IngredientAdapter ingredientAdapter;
    RecyclerView ordersRecyclerView;
    OrderAdapter orderAdapter;
    List<IngredientOrder> orderList = new ArrayList<>();

    private TabLayout tabLayout;

    private int currentTab = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        initViews(view);
//        setupRecyclerView();
//        setupSearchView();
//        setupClickListeners();

//        apiService = ApiClient.getClient().create(ApiService.class);

//        loadIngredients();
//        updateCartBadge();
        setupTabs();
        return view;
    }

    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.StatusTabsLayout);
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(getContext(), orderList);
        ordersRecyclerView.setAdapter(orderAdapter);

        // Thêm sự kiện click cho icon cart
        View cartIcon = view.findViewById(R.id.cartIcon);
        if (cartIcon != null) {
            cartIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CartActivity.class);
                    startActivity(intent);
                }
            });
        }
    }


    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Đang giao"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã giao"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                loadUserOrders();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Gọi API lần đầu luôn
        loadUserOrders();
    }


    private void loadUserOrders() {
        String status = currentTab == 0 ? "shipping" : "delivered";

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<OrderListResponse> call = apiService.getOrdersByStatus(status);

        call.enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderListResponse> call, @NonNull Response<OrderListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body().getOrders());
                    orderAdapter.notifyDataSetChanged(); // Hiển thị danh sách
                } else {
                    Toast.makeText(getContext(), "Không thể tải đơn hàng " + status, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderListResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void onAddToCartClick(Ingredient ingredient) {
//        // Add to local cart
//        boolean found = false;
//        for (Ingredient item : cartItems) {
//            if (item.getId().equals(ingredient.getId())) {
//                item.setQuantity(item.getQuantity() + 1);
//                found = true;
//                break;
//            }
//        }
//
//        if (!found) {
//            Ingredient cartItem = new Ingredient(ingredient);
//            cartItem.setQuantity(1);
//            cartItems.add(cartItem);
//        }
//
//        // Save to SharedPreferences or local database
//        saveCartToLocal();
//        updateCartBadge();
//
//        Toast.makeText(getContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
//    }

//    private void saveCartToLocal() {
//        // Implementation to save cart items to SharedPreferences
//        // This would typically use Gson to serialize the cart items
//    }
//
//    private void updateCartBadge() {
//        if (cartItems.size() > 0) {
//            BadgeDrawable badge = BadgeDrawable.create(getContext());
//            badge.setNumber(cartItems.size());
//            badge.setVisible(true);
//            // Apply badge to cart FAB
//        }
//    }
//
//    private void showError(String message) {
//        if (getContext() != null) {
//            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
//        loadIngredients();
//        updateCartBadge();
    }
}