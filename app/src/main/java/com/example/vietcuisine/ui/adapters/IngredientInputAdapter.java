package com.example.vietcuisine.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import java.util.List;

public class IngredientInputAdapter extends RecyclerView.Adapter<IngredientInputAdapter.IngredientViewHolder> {
    private List<String> ingredients;
    private OnIngredientRemovedListener listener;

    public interface OnIngredientRemovedListener {
        void onIngredientRemoved(int position);
    }

    public IngredientInputAdapter(List<String> ingredients, OnIngredientRemovedListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient_input, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        String ingredient = ingredients.get(position);
        holder.ingredientInput.setText(ingredient);
        
        holder.ingredientInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                ingredients.set(position, holder.ingredientInput.getText().toString());
            }
        });

        holder.removeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIngredientRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        EditText ingredientInput;
        ImageButton removeButton;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientInput = itemView.findViewById(R.id.ingredientInput);
            removeButton = itemView.findViewById(R.id.removeIngredientButton);
        }
    }
}
