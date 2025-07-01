package com.example.vietcuisine.ui.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.IngredientOrder;
import com.example.vietcuisine.data.model.OrderListResponse;
import com.example.vietcuisine.data.model.OrderRequest;
import com.example.vietcuisine.data.model.PaymentResponse;
import com.example.vietcuisine.data.model.RecipeIngredient;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.ui.adapters.RecipeIngredientAdapter;
import com.example.vietcuisine.data.model.IngredientOrder.ShippingAddress;
import com.example.vietcuisine.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView ingredientsRecyclerView;
    private RecipeIngredientAdapter ingredientAdapter;
    private TextView tvTotal;
    private Button btnBuy;
    private RadioButton cashButton, cardButton;
    private String paymentMethod;
    private EditText ricipientName, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Handle back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        ricipientName= findViewById(R.id.etCustomer);
        phone = findViewById(R.id.etPhone);
        address=findViewById(R.id.etAddress);
        tvTotal = findViewById(R.id.tvTotal);
        btnBuy=findViewById(R.id.btnBuy);
        cashButton=findViewById(R.id.rbCash);
        cardButton=findViewById(R.id.rbCard);
        cashButton.setOnClickListener(v-> paymentMethod="cash");
        cardButton.setOnClickListener(v->paymentMethod="credit_card");

        btnBuy.setOnClickListener(v -> {
            if (paymentMethod == null || paymentMethod.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }
            payment();
        });

        ArrayList<RecipeIngredient> selectedIngredients = (ArrayList<RecipeIngredient>) getIntent().getSerializableExtra("selected_ingredients");
        Log.d("selet","ingredient selet"+selectedIngredients);
        ingredientAdapter = new RecipeIngredientAdapter(selectedIngredients, true, total -> {
            tvTotal.setText(String.format("Tổng: %.0f VND", total));
        });
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        updateTotalPrice();
        // TODO: Implement other order functionality (user info, payment, etc.)
    }

    private void updateTotalPrice() {
        double total = 0;
        if (ingredientAdapter != null) {
            for (RecipeIngredient ingredient : ingredientAdapter.getSelectedIngredients()) {
                String quantityStr = ingredient.getQuantity();
                float quantity = 1f;
                Matcher matcher = Pattern.compile("(\\d+(?:\\.\\d+)?)").matcher(quantityStr.trim());
                if (matcher.find()) {
                    try {
                        quantity = Float.parseFloat(matcher.group(1));
                    } catch (Exception ignored) {}
                }
                double unitPrice = ingredient.getUnitPrice();
                total += unitPrice * quantity;
            }
        }
        tvTotal.setText(String.format("Tổng: %.0f VND", total));
    }


    private void payment() {
        List<RecipeIngredient> selectedIngredients = ingredientAdapter.getSelectedIngredients();
        if (selectedIngredients == null || selectedIngredients.isEmpty()) {
            Toast.makeText(this, "Bạn chưa chọn nguyên liệu!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<OrderRequest.OrderItem> orderItems = new ArrayList<>();
        for (RecipeIngredient ingredient : selectedIngredients) {
            String ingredientId = ingredient.getIngredientId();
            int quantity = extractQuantity(ingredient.getQuantity());
            orderItems.add(new OrderRequest.OrderItem(ingredientId, quantity));
        }

        String nameStr = ricipientName.getText().toString().trim();
        String phoneStr = phone.getText().toString().trim();
        String addressStr = address.getText().toString().trim();

        if (nameStr.isEmpty() || phoneStr.isEmpty() || addressStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        IngredientOrder.ShippingAddress shippingAddress = new IngredientOrder.ShippingAddress(
                nameStr,
                phoneStr,
                addressStr
        );

        OrderRequest orderRequest = new OrderRequest(orderItems, shippingAddress, paymentMethod);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.createOrder(orderRequest).enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String orderId = response.body().getOrderId();

                    if (paymentMethod.equals("credit_card")) {
                        Map<String, String> body = new HashMap<>();
                        body.put("orderId", orderId);

                        apiService.processPayment(body).enqueue(new Callback<PaymentResponse>() {
                            @Override
                            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> paymentResponse) {
                                if (paymentResponse.isSuccessful() && paymentResponse.body() != null) {
                                    String redirectUrl = paymentResponse.body().getUrl();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl));
                                    startActivity(browserIntent);
                                    Toast.makeText(OrderActivity.this, "Đang chuyển đến trang thanh toán...", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(OrderActivity.this, "Thanh toán thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                                Toast.makeText(OrderActivity.this, "Lỗi thanh toán: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if (paymentMethod.equals("cash")) {
                        Toast.makeText(OrderActivity.this, "Đặt hàng thành công bằng tiền mặt!", Toast.LENGTH_SHORT).show();

                        // Trở về MainActivity và mở ShopFragment
                        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                        intent.putExtra("navigateTo", "shop");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Toast.makeText(OrderActivity.this, "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int extractQuantity(String quantityStr) {
        Matcher matcher = Pattern.compile("(\\d+)").matcher(quantityStr.trim());
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (Exception ignored) {}
        }
        return 1;
    }
}