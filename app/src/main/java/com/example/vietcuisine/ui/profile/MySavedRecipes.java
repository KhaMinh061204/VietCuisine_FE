package com.example.vietcuisine.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.Recipe;
import com.example.vietcuisine.data.model.RecipeResponse;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.ui.adapters.ProfileRecipeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySavedRecipes extends Fragment {

    private RecyclerView recyclerView;
    private ProfileRecipeAdapter adapter;
    private List<Recipe> recipeList = new ArrayList<>();
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_saved_recipes, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSavedRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProfileRecipeAdapter(getContext(), recipeList, recipe -> {
            // Handle item click
        });

        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        //loadSavedRecipes();

        return view;
    }

    private void loadSavedRecipes() {
        apiService.getSavedRecipes().enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipeList.clear();
                    recipeList.addAll(response.body().getRecipes());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải công thức đã lưu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
