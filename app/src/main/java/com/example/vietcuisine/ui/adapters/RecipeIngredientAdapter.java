package com.example.vietcuisine.ui.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.RecipeIngredient;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {

    private List<RecipeIngredient> ingredientList;
    private boolean isOrderMode = false;
    private OnTotalPriceChanged onTotalPriceChanged;

    public interface OnTotalPriceChanged {
        void onChanged(double total);
    }

    public RecipeIngredientAdapter(List<RecipeIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public RecipeIngredientAdapter(List<RecipeIngredient> ingredientList, boolean isOrderMode) {
        this.ingredientList = ingredientList;
        this.isOrderMode = isOrderMode;
    }

    public RecipeIngredientAdapter(List<RecipeIngredient> ingredientList, boolean isOrderMode, OnTotalPriceChanged onTotalPriceChanged) {
        this.ingredientList = ingredientList;
        this.isOrderMode = isOrderMode;
        this.onTotalPriceChanged = onTotalPriceChanged;
    }

    public void updateIngredients(List<RecipeIngredient> newList) {
        this.ingredientList = newList;
        notifyDataSetChanged();
        calculateTotalPrice();
    }

    public List<RecipeIngredient> getSelectedIngredients() {
        List<RecipeIngredient> selectedList = new ArrayList<>();
        for (RecipeIngredient ingredient : ingredientList) {
            if (isOrderMode || ingredient.isSelected()) {
                selectedList.add(ingredient);
            }
        }
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

        Glide.with(holder.itemView.getContext())
                .load(ingredient.getImageUrl())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(holder.ingredientImage);

        if (isOrderMode) {
            holder.checkBox.setVisibility(View.GONE);
            holder.etQuantity.setVisibility(View.VISIBLE);
            holder.tvUnit.setVisibility(View.VISIBLE);
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.quantityTextView.setVisibility(View.GONE);

            // Tách số lượng và đơn vị
            String quantityStr = ingredient.getQuantity();
            float quantity = 1f;
            String unit = "";
            Matcher matcher = Pattern.compile("(\\d+(?:\\.\\d+)?)(.*)").matcher(quantityStr.trim());
            if (matcher.matches()) {
                try {
                    quantity = Float.parseFloat(matcher.group(1));
                } catch (Exception ignored) {}
                unit = matcher.group(2).trim();
            }

            holder.etQuantity.setText(String.valueOf(quantity));
            holder.tvUnit.setText(ingredient.getUnit());

            double unitPrice = ingredient.getUnitPrice();
            holder.tvPrice.setText(String.format("%.0f VND", unitPrice * quantity));

            // Theo dõi thay đổi số lượng
            holder.etQuantity.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(Editable s) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    float newQuantity = 1f;
                    try {
                        newQuantity = Float.parseFloat(s.toString());
                    } catch (Exception ignored) {}

                    if (newQuantity < 0.01f) newQuantity = 0.01f;
                    ingredient.setQuantity(newQuantity + " " + ingredient.getUnit());

                    double price = unitPrice * newQuantity;
                    holder.tvPrice.setText(String.format("%.0f VND", price));

                    // ✅ Tính lại tổng tiền
                    calculateTotalPrice();
                }
            });

        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.etQuantity.setVisibility(View.GONE);
            holder.tvUnit.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
            holder.quantityTextView.setVisibility(View.VISIBLE);
            holder.quantityTextView.setText(ingredient.getQuantity());

            holder.checkBox.setChecked(ingredient.isSelected());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                ingredient.setSelected(isChecked);
            });
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList != null ? ingredientList.size() : 0;
    }

    private void calculateTotalPrice() {
        double total = 0;
        for (RecipeIngredient ingredient : getSelectedIngredients()) {
            float quantity = 1f;

            Matcher matcher = Pattern.compile("(\\d+(?:\\.\\d+)?)(.*)").matcher(ingredient.getQuantity().trim());
            if (matcher.matches()) {
                try {
                    quantity = Float.parseFloat(matcher.group(1));
                } catch (Exception ignored) {}
            }

            total += quantity * ingredient.getUnitPrice();
        }

        if (onTotalPriceChanged != null) {
            onTotalPriceChanged.onChanged(total);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView nameTextView, quantityTextView;
        EditText etQuantity;
        TextView tvUnit, tvPrice;
        ImageView ingredientImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox3);
            nameTextView = itemView.findViewById(R.id.ingredientName);
            quantityTextView = itemView.findViewById(R.id.ingredientQuantity);
            etQuantity = itemView.findViewById(R.id.etQuantity);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
        }
    }
}
