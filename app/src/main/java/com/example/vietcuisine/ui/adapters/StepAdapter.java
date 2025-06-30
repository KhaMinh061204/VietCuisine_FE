package com.example.vietcuisine.ui.adapters;

import android.text.Editable;
import android.text.TextWatcher;
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
        // Gỡ TextWatcher cũ nếu có (tránh bị nhân đôi)
        if (holder.currentWatcher != null) {
            holder.stepInput.removeTextChangedListener(holder.currentWatcher);
        }

        // Thiết lập dữ liệu hiện tại
        holder.stepInput.setText(steps.get(position));

        // Tạo TextWatcher mới
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    steps.set(adapterPosition, s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        // Gán watcher và lưu tham chiếu
        holder.stepInput.addTextChangedListener(watcher);
        holder.currentWatcher = watcher;

        holder.removeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStepRemoved(holder.getAdapterPosition());
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
        TextWatcher currentWatcher;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            stepInput = itemView.findViewById(R.id.stepInput);
            removeButton = itemView.findViewById(R.id.removeStepButton);
        }
    }
}