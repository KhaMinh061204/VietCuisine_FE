package com.example.vietcuisine.ui.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vietcuisine.ui.fragments.ShopFragment;
import com.example.vietcuisine.ui.main.MainActivity;

public class OrderResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lấy orderId từ URL
        Uri data = getIntent().getData();
        Log.d("log data","data"+data);
        if (data != null && "payment-success".equals(data.getHost())) {
            String orderId = data.getQueryParameter("orderId");
            Log.d("order success","order"+orderId);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("navigate_to", "shop"); // thông báo cho MainActivity biết cần chuyển đến ShopFragment
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Thanh toán bị hủy hoặc không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
