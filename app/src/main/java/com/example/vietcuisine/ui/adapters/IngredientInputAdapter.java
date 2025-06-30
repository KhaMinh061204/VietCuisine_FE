package com.example.vietcuisine.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Ingredient;
import com.example.vietcuisine.data.model.IngredientInput;
import com.example.vietcuisine.data.model.IngredientResponse;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientInputAdapter extends RecyclerView.Adapter<IngredientInputAdapter.IngredientViewHolder> {

    private final List<IngredientInput> ingredients;
    private final Context context;
    private final ApiService apiService;
    private final OnIngredientRemovedListener listener;

    public interface OnIngredientRemovedListener {
        void onIngredientRemoved(int position);
    }

    public IngredientInputAdapter(Context context, List<IngredientInput> ingredients, OnIngredientRemovedListener listener) {
        this.context = context;
        this.ingredients = ingredients;
        this.listener = listener;
        this.apiService = ApiClient.getClient().create(ApiService.class);
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
        IngredientInput item = ingredients.get(position);

        holder.ingredientInput.setText(item.getIngredient());
        holder.quantityInput.setText(item.getQuantity());

        // Cập nhật khi người dùng thay đổi nội dung
        holder.ingredientInput.addTextChangedListener(new TextWatcher() {
            private final Handler handler = new Handler(Looper.getMainLooper());
            private Runnable searchRunnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                item.setIngredient(s.toString());

                if (searchRunnable != null) handler.removeCallbacks(searchRunnable);

                searchRunnable = () -> {
                    if (s.length() >= 2) {
                        searchIngredients(s.toString(), holder.ingredientInput);
                    }
                };
                handler.postDelayed(searchRunnable, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setIngredient(s.toString());
            }
        });

        holder.quantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                item.setQuantity(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setQuantity(s.toString());
            }
        });

        holder.ingredientInput.setOnItemClickListener((parent, view, pos, id) -> {
            String selected = (String) parent.getItemAtPosition(pos);
            holder.ingredientInput.setText(selected);
            item.setIngredient(selected);
        });

        holder.removeButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (listener != null && currentPosition != RecyclerView.NO_POSITION) {
                listener.onIngredientRemoved(currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        AutoCompleteTextView ingredientInput;
        EditText quantityInput;
        ImageButton removeButton;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientInput = itemView.findViewById(R.id.ingredientSearchInput);
            quantityInput = itemView.findViewById(R.id.quantityInput);
            removeButton = itemView.findViewById(R.id.removeIngredientButton);
        }
    }

    private void searchIngredients(String query, AutoCompleteTextView view) {
        apiService.searchIngredients(query).enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    List<String> names = new ArrayList<>();
                    for (Ingredient ing : response.body().getIngredients()) {
                        names.add(ing.getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            context,
                            android.R.layout.simple_dropdown_item_1line,
                            names
                    );
                    view.setAdapter(adapter);
                    view.showDropDown();
                }
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                // Có thể log lỗi ở đây nếu cần
            }
        });
    }
}
