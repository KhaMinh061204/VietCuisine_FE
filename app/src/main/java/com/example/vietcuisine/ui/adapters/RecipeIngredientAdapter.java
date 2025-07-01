package com.example.vietcuisine.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.RecipeIngredient;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {

    private List<RecipeIngredient> ingredientList;
    private boolean isOrderMode = false;
    private Runnable onUpdateTotalPrice;

    public RecipeIngredientAdapter(List<RecipeIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    // New constructor for order mode
    public RecipeIngredientAdapter(List<RecipeIngredient> ingredientList, boolean isOrderMode) {
        this.ingredientList = ingredientList;
        this.isOrderMode = isOrderMode;
    }

    public RecipeIngredientAdapter(List<RecipeIngredient> ingredientList, boolean isOrderMode, Runnable onUpdateTotalPrice) {
        this.ingredientList = ingredientList;
        this.isOrderMode = isOrderMode;
        this.onUpdateTotalPrice = onUpdateTotalPrice;
    }

    public void updateIngredients(List<RecipeIngredient> newList) {
        this.ingredientList = newList;
        notifyDataSetChanged();
    }

    public List<RecipeIngredient> getSelectedIngredients() {
        List<RecipeIngredient> selectedList = new ArrayList<>();
        for (RecipeIngredient ingredient : ingredientList) {
            if (ingredient.isSelected()) {
                selectedList.add(ingredient);
            }
        }
        Log.d("selected list name","list name"+selectedList.get(0));

        Log.d("selected list name","list name"+selectedList.get(0).getName());
        Log.d("selected list id","list id"+selectedList.get(0).getIngredientId());

        return selectedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeIngredient ingredient = ingredientList.get(position);
        holder.nameTextView.setText(ingredient.getName());
        holder.quantityTextView.setText(ingredient.getQuantity());
        // Load ingredient image
        ImageView ingredientImage = holder.itemView.findViewById(R.id.ingredientImage);
        String imageUrl = null;
        if (ingredient instanceof com.example.vietcuisine.data.model.RecipeIngredient) {
            try {
                java.lang.reflect.Method getImageUrl = ingredient.getClass().getMethod("getImageUrl");
                Object url = getImageUrl.invoke(ingredient);
                if (url != null) imageUrl = url.toString();
            } catch (Exception ignored) {}
        }
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(ingredientImage);
        } else {
            ingredientImage.setImageResource(R.drawable.ic_avatar_placeholder);
        }

        // Remove checkBox logic for order mode
        // holder.checkBox.setChecked(ingredient.isSelected());

        // Views for order mode
        TextView tvUnit = holder.itemView.findViewById(R.id.tvUnit);
        EditText etQuantity = holder.itemView.findViewById(R.id.etQuantity);
        TextView tvPrice = holder.itemView.findViewById(R.id.tvPrice);

        // Tách số lượng và đơn vị
        String quantityStr = ingredient.getQuantity();
        int quantity = 1;
        String unit = "";
        Matcher matcher = Pattern.compile("(\\d+)(.*)").matcher(quantityStr.trim());
        if (matcher.matches()) {
            try { quantity = Integer.parseInt(matcher.group(1)); } catch (Exception ignored) {}
            unit = matcher.group(2).trim();
        } else {
            try { quantity = Integer.parseInt(quantityStr.trim()); } catch (Exception ignored) {}
        }

        final String finalUnit = unit;
        if (isOrderMode) {
            holder.checkBox.setVisibility(View.GONE); // Hide checkbox in order mode
            etQuantity.setVisibility(View.VISIBLE);
            tvUnit.setVisibility(View.VISIBLE);
            tvPrice.setVisibility(View.VISIBLE);
            holder.quantityTextView.setVisibility(View.GONE);
            // Remove checkBox selection logic
            etQuantity.setText(String.valueOf(quantity));
            tvUnit.setText(finalUnit);

            // Hiển thị giá tiền
            double unitPrice = ingredient.getUnitPrice();
            try {
                int q = Integer.parseInt(etQuantity.getText().toString());
                double price = unitPrice * q;
                tvPrice.setText(String.format("%.0f VND", price));
            } catch (Exception e) {
                tvPrice.setText("0 VND");
            }
            etQuantity.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int newQuantity = 1;
                    try {
                        newQuantity = Integer.parseInt(s.toString());
                    } catch (Exception ignored) {}

                    if (newQuantity < 1) newQuantity = 1;

                    // Cập nhật model
                    ingredient.setQuantity(newQuantity + (finalUnit.isEmpty() ? "" : finalUnit));

                    // Cập nhật UI
                    double price = unitPrice * newQuantity;
                    tvPrice.setText(String.format("%.0f VND", price));

                    // Cập nhật tổng tiền
                    if (onUpdateTotalPrice != null) onUpdateTotalPrice.run();
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {}
            });
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            etQuantity.setVisibility(View.GONE);
            tvUnit.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
            holder.quantityTextView.setVisibility(View.VISIBLE);
            holder.quantityTextView.setText(quantityStr);
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                ingredient.setSelected(isChecked);
            });
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList != null ? ingredientList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView nameTextView, quantityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox3);
            nameTextView = itemView.findViewById(R.id.ingredientName);
            quantityTextView = itemView.findViewById(R.id.ingredientQuantity);
        }
    }
}