package com.example.vietcuisine.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Ingredient;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private List<Ingredient> ingredients;
    private Context context;
    private OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
        void onAddToCartClick(Ingredient ingredient);
    }

    public IngredientAdapter(Context context, List<Ingredient> ingredients, OnIngredientClickListener listener) {
        this.context = context;
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
          holder.ingredientName.setText(ingredient.getName());
        holder.ingredientPrice.setText(String.format("%,.0f VND", ingredient.getUnitPrice()));
          if (ingredient.getCategory() != null && !ingredient.getCategory().isEmpty()) {
            holder.ingredientDescription.setText(ingredient.getCategory());
            holder.ingredientDescription.setVisibility(View.VISIBLE);
        } else {
            holder.ingredientDescription.setVisibility(View.GONE);
        }

        // TODO: Load image with Glide or Picasso
        // Glide.with(context).load(ingredient.getImage()).into(holder.ingredientImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIngredientClick(ingredient);
            }
        });

        holder.addToCartButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClick(ingredient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void updateIngredients(List<Ingredient> newIngredients) {
        this.ingredients = newIngredients;
        notifyDataSetChanged();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredientImage;
        TextView ingredientName, ingredientDescription, ingredientPrice;
        ImageButton addToCartButton;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            ingredientDescription = itemView.findViewById(R.id.ingredientDescription);
            ingredientPrice = itemView.findViewById(R.id.ingredientPrice);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
