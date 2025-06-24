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

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private List<String> steps;
    private OnStepRemovedListener listener;

    public interface OnStepRemovedListener {
        void onStepRemoved(int position);
    }

    public StepAdapter(List<String> steps, OnStepRemovedListener listener) {
        this.steps = steps;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step_input, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        String step = steps.get(position);
        holder.stepInput.setText(step);
        
        holder.stepInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                steps.set(position, holder.stepInput.getText().toString());
            }
        });

        holder.removeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStepRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    static class StepViewHolder extends RecyclerView.ViewHolder {
        EditText stepInput;
        ImageButton removeButton;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            stepInput = itemView.findViewById(R.id.stepInput);
            removeButton = itemView.findViewById(R.id.removeStepButton);
        }
    }
}
