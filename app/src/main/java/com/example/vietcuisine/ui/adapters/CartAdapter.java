package com.example.vietcuisine.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.CheckBox;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.RecipeIngredient;
import java.util.List;
import android.text.Editable;
import android.text.TextWatcher;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<RecipeIngredient> cartItems;
    private Runnable onUpdateTotalPrice;

    public CartAdapter(List<RecipeIngredient> cartItems, Runnable onUpdateTotalPrice) {
        this.cartItems = cartItems;
        this.onUpdateTotalPrice = onUpdateTotalPrice;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeIngredient item = cartItems.get(position);
        holder.nameTextView.setText(item.getName());
        // Parse quantity and unit
        String quantityStr = item.getQuantity();
        int quantity = 1;
        final String[] unit = {""};
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d+)(.*)").matcher(quantityStr.trim());
        if (matcher.matches()) {
            try { quantity = Integer.parseInt(matcher.group(1)); } catch (Exception ignored) {}
            unit[0] = matcher.group(2).trim();
        } else {
            try { quantity = Integer.parseInt(quantityStr.trim()); } catch (Exception ignored) {}
        }
        holder.etQuantity.setText(String.valueOf(quantity));
        holder.tvUnit.setText(unit[0]);
        holder.ingredientQuantity.setVisibility(View.GONE);
        holder.tvPrice.setVisibility(View.VISIBLE);
        double price = item.getUnitPrice() * quantity;
        holder.tvPrice.setText(String.format("%.0f VND", price));
        // Image logic
        String imageUrl = null;
        try {
            java.lang.reflect.Method getImageUrl = item.getClass().getMethod("getImageUrl");
            Object url = getImageUrl.invoke(item);
            if (url != null) imageUrl = url.toString();
        } catch (Exception ignored) {}
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(holder.ingredientImage);
        } else {
            holder.ingredientImage.setImageResource(R.drawable.ic_avatar_placeholder);
        }
        // Checkbox logic
        holder.checkBox.setChecked(item.isSelected());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
            if (onUpdateTotalPrice != null) onUpdateTotalPrice.run();
        });
        // Remove item logic
        holder.btnRemove.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                cartItems.remove(pos);
                notifyItemRemoved(pos);
                if (onUpdateTotalPrice != null) onUpdateTotalPrice.run();
            }
        });
        // Listen for quantity changes
        holder.etQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String input = holder.etQuantity.getText().toString();
                int newQuantity = 1;
                try { newQuantity = Integer.parseInt(input); } catch (Exception ignored) {}
                if (newQuantity < 1) newQuantity = 1;
                holder.etQuantity.setText(String.valueOf(newQuantity));
                item.setQuantity(newQuantity + (unit[0].isEmpty() ? "" : unit[0]));
                double newPrice = item.getUnitPrice() * newQuantity;
                holder.tvPrice.setText(String.format("%.0f VND", newPrice));
                if (onUpdateTotalPrice != null) onUpdateTotalPrice.run();
            }
        });
        holder.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (holder.etQuantity.hasFocus()) {
                    String input = s.toString();
                    int newQuantity = 1;
                    try { newQuantity = Integer.parseInt(input); } catch (Exception ignored) {}
                    if (newQuantity < 1) newQuantity = 1;
                    item.setQuantity(newQuantity + (unit[0].isEmpty() ? "" : unit[0]));
                    double newPrice = item.getUnitPrice() * newQuantity;
                    holder.tvPrice.setText(String.format("%.0f VND", newPrice));
                    if (onUpdateTotalPrice != null) onUpdateTotalPrice.run();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredientImage;
        TextView nameTextView, tvUnit, ingredientQuantity, tvPrice;
        EditText etQuantity;
        CheckBox checkBox;
        ImageButton btnRemove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            nameTextView = itemView.findViewById(R.id.ingredientName);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            ingredientQuantity = itemView.findViewById(R.id.ingredientQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            etQuantity = itemView.findViewById(R.id.etQuantity);
            checkBox = itemView.findViewById(R.id.checkBox3);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
