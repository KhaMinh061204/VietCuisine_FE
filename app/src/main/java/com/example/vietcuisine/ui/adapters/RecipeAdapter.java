package com.example.vietcuisine.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    
    private List<Recipe> recipes;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_horizontal, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImage;
        private TextView recipeTitle, recipeDescription, cookingTime, likesCount;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeDescription = itemView.findViewById(R.id.recipeDescription);
            cookingTime = itemView.findViewById(R.id.cookingTime);
            likesCount = itemView.findViewById(R.id.likesCount);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRecipeClick(recipes.get(position));
                    }
                }
            });
        }

        public void bind(Recipe recipe) {
            recipeTitle.setText(recipe.getTitle());
            recipeDescription.setText(recipe.getDescription());
            cookingTime.setText(recipe.getTime() + " phút");
            likesCount.setText(String.valueOf(recipe.getLikesCount()));
            
            if (recipe.getImage() != null && !recipe.getImage().isEmpty()) {
                Glide.with(itemView.getContext())
                    .load(recipe.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_recipe)
                    .into(recipeImage);
            } else {
                recipeImage.setImageResource(R.drawable.placeholder_recipe);
            }
        }
    }
}
