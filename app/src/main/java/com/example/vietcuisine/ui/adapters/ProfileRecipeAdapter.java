package com.example.vietcuisine.ui.adapters;

import android.content.Context;
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

public class ProfileRecipeAdapter extends RecyclerView.Adapter<ProfileRecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private Context context;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public ProfileRecipeAdapter(Context context, List<Recipe> recipes, OnRecipeClickListener listener) {
        this.context = context;
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe_grid, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.recipeTitle.setText(recipe.getTitle());

        if (recipe.getImage() != null && !recipe.getImage().isEmpty()) {
            Glide.with(context)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.placeholder_post)
                    .into(holder.recipeImage);
            holder.recipeImage.setVisibility(View.VISIBLE);
        } else {
            holder.recipeImage.setVisibility(View.GONE);
        }


        holder.cookingTime.setText(recipe.getTime() + " phút");



        holder.likesCount.setText(String.valueOf(recipe.getLikesCount()));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void updateRecipes(List<Recipe> newRecipes) {
        this.recipes = newRecipes;
        notifyDataSetChanged();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeTitle;
        TextView cookingTime, likesCount;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            cookingTime = itemView.findViewById(R.id.cookingTime);
            likesCount = itemView.findViewById(R.id.likesCount);
        }
    }
}
