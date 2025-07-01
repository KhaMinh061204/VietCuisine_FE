package com.example.vietcuisine.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.IngredientOrder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<IngredientOrder> orderList;

    public OrderAdapter(Context context, List<IngredientOrder> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        IngredientOrder order = orderList.get(position);

        Log.d("holder","order"+order.getId()+","+order.getPaymentStatus()+","+getTotalItems(order)+","+formatCurrency(order.getTotalCost()));
        holder.tvOrderId.setText("Mã đơn: " + order.getId());
        if("pending".equals(order.getPaymentStatus())){
        holder.tvPaymentStatus.setText("Trạng thái: Chờ thanh toán");}
        if("paid".equals(order.getPaymentStatus())){
            holder.tvPaymentStatus.setText("Trạng thái: Đã thanh toán");}
        holder.tvItemCount.setText("Tổng sản phẩm: " + getTotalItems(order));
        holder.tvTotalPrice.setText("Tổng tiền: " + formatCurrency(order.getTotalCost()));
    }

    private int getTotalItems(IngredientOrder order) {
        int total = 0;
        for (IngredientOrder.OrderItem item : order.getItems()) {
            total += item.getQuantity();
        }
        return total;
    }

    private String formatCurrency(BigDecimal amount) {
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        return formatter.format(amount);
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvTotalPrice, tvItemCount, tvPaymentStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvItemCount = itemView.findViewById(R.id.tvTotalItems);
            tvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
        }
    }
}
