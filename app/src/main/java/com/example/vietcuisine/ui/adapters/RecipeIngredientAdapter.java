package com.example.vietcuisine.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.RecipeIngredient;
import java.util.List;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {
    private List<RecipeIngredient> ingredients;

    public RecipeIngredientAdapter(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
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
        RecipeIngredient ingredient = ingredients.get(position);
        
        holder.ingredientName.setText(ingredient.getIngredientName() != null ? 
            ingredient.getIngredientName() : "Nguyên liệu");
        
        String quantity = "";
        if (ingredient.getQuantity() > 0) {
            quantity += ingredient.getQuantity();
        }
        if (ingredient.getUnit() != null && !ingredient.getUnit().isEmpty()) {
            quantity += " " + ingredient.getUnit();
        }
        
        holder.ingredientQuantity.setText(quantity.isEmpty() ? "Không xác định" : quantity);
    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    public void updateIngredients(List<RecipeIngredient> newIngredients) {
        this.ingredients = newIngredients;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName, ingredientQuantity;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            ingredientQuantity = itemView.findViewById(R.id.ingredientQuantity);
        }
    }
}
