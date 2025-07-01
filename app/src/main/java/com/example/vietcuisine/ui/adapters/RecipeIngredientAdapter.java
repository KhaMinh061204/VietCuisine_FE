package com.example.vietcuisine.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.RecipeIngredient;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {

    private List<RecipeIngredient> ingredientList;
    private boolean isOrderMode = false;

    public RecipeIngredientAdapter(List<RecipeIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    // New constructor for order mode
    public RecipeIngredientAdapter(List<RecipeIngredient> ingredientList, boolean isOrderMode) {
        this.ingredientList = ingredientList;
        this.isOrderMode = isOrderMode;
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
        holder.checkBox.setChecked(ingredient.isSelected());

        // Views for order mode
        TextView tvUnit = holder.itemView.findViewById(R.id.tvUnit);
        EditText etQuantity = holder.itemView.findViewById(R.id.etQuantity);

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
            holder.checkBox.setVisibility(View.VISIBLE);
            etQuantity.setVisibility(View.VISIBLE);
            tvUnit.setVisibility(View.VISIBLE);
            holder.quantityTextView.setVisibility(View.GONE);
            holder.checkBox.setChecked(ingredient.isSelected());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                ingredient.setSelected(isChecked);
            });
            etQuantity.setText(String.valueOf(quantity));
            tvUnit.setText(finalUnit);

            etQuantity.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    String input = etQuantity.getText().toString();
                    int newQuantity = 1;
                    try { newQuantity = Integer.parseInt(input); } catch (Exception ignored) {}
                    if (newQuantity < 1) newQuantity = 1;
                    etQuantity.setText(String.valueOf(newQuantity));
                    ingredient.setQuantity(newQuantity + (finalUnit.isEmpty() ? "" : finalUnit));
                }
            });
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            etQuantity.setVisibility(View.GONE);
            tvUnit.setVisibility(View.GONE);
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