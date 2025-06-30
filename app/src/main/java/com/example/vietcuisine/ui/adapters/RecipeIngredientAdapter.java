package com.example.vietcuisine.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.RecipeIngredient;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {

    private List<RecipeIngredient> ingredientList;

    public RecipeIngredientAdapter(List<RecipeIngredient> ingredientList) {
        this.ingredientList = ingredientList;
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

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ingredient.setSelected(isChecked);
        });
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