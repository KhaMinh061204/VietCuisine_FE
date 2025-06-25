package com.example.vietcuisine.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vietcuisine.R;
import java.util.List;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {
    private List<String> steps;

    public RecipeStepAdapter(List<String> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String step = steps.get(position);
        
        holder.stepNumber.setText(String.valueOf(position + 1));
        holder.stepDescription.setText(step != null ? step : "Không có mô tả");
    }

    @Override
    public int getItemCount() {
        return steps != null ? steps.size() : 0;
    }

    public void updateSteps(List<String> newSteps) {
        this.steps = newSteps;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stepNumber, stepDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            stepNumber = itemView.findViewById(R.id.stepNumber);
            stepDescription = itemView.findViewById(R.id.stepDescription);
        }
    }
}
