package com.example.vietcuisine.ui.recipe;

import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.model.*;
import com.example.vietcuisine.data.network.*;
import com.example.vietcuisine.ui.adapters.IngredientInputAdapter;
import com.example.vietcuisine.ui.adapters.StepAdapter;
import com.example.vietcuisine.utils.FileUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.util.*;
import okhttp3.*;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRecipeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView recipeImageView, backButton;
    private TextInputEditText titleInput, descriptionInput, cookingTimeInput, caloriesInput, proteinInput, carbsInput, fatInput;
    private AutoCompleteTextView categoryInput;
    private RecyclerView ingredientsRecyclerView, stepsRecyclerView;
    private TextView addIngredientButton, addStepButton;
    private MaterialButton publishButton;
    private ProgressBar progressBar;

    private IngredientInputAdapter ingredientInputAdapter;
    private StepAdapter stepAdapter;
    private List<String> steps;
    private List<IngredientInput> ingredients;
    private List<Category> categories;
    private Category selectedCategory;
    private Uri selectedImageUri;

    private ApiService apiService;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        apiService = ApiClient.getClient().create(ApiService.class);

        initViews();
        setupRecyclerViews();
        setupClickListeners();
        loadCategories();
    }

    private void initViews() {
        recipeImageView = findViewById(R.id.recipeImageView);
        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        cookingTimeInput = findViewById(R.id.cookTimeInput);
        caloriesInput = findViewById(R.id.caloriesInput);
        proteinInput = findViewById(R.id.proteinInput);
        carbsInput = findViewById(R.id.carbsInput);
        fatInput = findViewById(R.id.fatInput);
        categoryInput = findViewById(R.id.categoryInput);
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        stepsRecyclerView = findViewById(R.id.stepsRecyclerView);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        addStepButton = findViewById(R.id.addStepButton);
        publishButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.backButton);
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
        categories = new ArrayList<>();

        ingredients.add(new IngredientInput("", ""));
        steps.add("");
    }

    private void setupRecyclerViews() {
        stepAdapter = new StepAdapter(steps, this::onStepRemoved);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepsRecyclerView.setAdapter(stepAdapter);

        ingredientInputAdapter = new IngredientInputAdapter(this, ingredients, this::onIngredientRemoved);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientInputAdapter);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        recipeImageView.setOnClickListener(v -> openImagePicker());

        addStepButton.setOnClickListener(v -> {
            steps.add("");
            stepAdapter.notifyItemInserted(steps.size() - 1);
        });

        addIngredientButton.setOnClickListener(v -> {
            ingredients.add(new IngredientInput("", ""));
            ingredientInputAdapter.notifyItemInserted(ingredients.size() - 1);
        });

        publishButton.setOnClickListener(v -> publishRecipe());
    }

    private void loadCategories() {
        categoryInput.setEnabled(false);
        categoryInput.setText("Đang tải danh mục...");

        apiService.getAllCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCategories() != null) {
                    categories.clear();
                    categories.addAll(response.body().getCategories());
                    setupCategoryDropdown();
                } else {
                    showCategoryLoadError("Không có danh mục hoặc lỗi server.");
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                showCategoryLoadError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void setupCategoryDropdown() {
        categoryInput.setEnabled(true);
        categoryInput.setText("");
        categoryInput.setHint("");

        List<String> categoryNames = new ArrayList<>();
        for (Category cat : categories) {
            categoryNames.add(cat.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categoryNames);
        categoryInput.setAdapter(adapter);

        categoryInput.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < categories.size()) {
                selectedCategory = categories.get(position);
                categoryInput.setText(selectedCategory.getName());
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Glide.with(this).load(selectedImageUri).centerCrop().into(recipeImageView);
        }
    }

    private void onStepRemoved(int position) {
        if (steps.size() > 1) {
            steps.remove(position);
            stepAdapter.notifyItemRemoved(position);
        }
    }

    private void onIngredientRemoved(int position) {
        if (ingredients.size() > 1) {
            ingredients.remove(position);
            ingredientInputAdapter.notifyItemRemoved(position);
        }
    }

    private void publishRecipe() {
        if (!validateInputs()) return;
        setLoading(true);

        RequestBody title = createPlainText(titleInput.getText().toString());
        RequestBody description = createPlainText(descriptionInput.getText().toString());
        RequestBody cookingTime = createPlainText(cookingTimeInput.getText().toString());
        RequestBody calories = createPlainText(caloriesInput.getText().toString());
        RequestBody protein = createPlainText(proteinInput.getText().toString());
        RequestBody carbs = createPlainText(carbsInput.getText().toString());
        RequestBody fat = createPlainText(fatInput.getText().toString());
        RequestBody category = createPlainText(selectedCategory != null ? selectedCategory.getId() : "");

        List<MultipartBody.Part> ingredientParts = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            IngredientInput ing = ingredients.get(i);
            ingredientParts.add(MultipartBody.Part.createFormData("ingredients[" + i + "][ingredient]", ing.getIngredient()));
            ingredientParts.add(MultipartBody.Part.createFormData("ingredients[" + i + "][quantity]", ing.getQuantity()));
        }

        List<MultipartBody.Part> stepParts = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            stepParts.add(MultipartBody.Part.createFormData("steps[" + i + "]", steps.get(i)));
        }

        MultipartBody.Part image = null;
        if (selectedImageUri != null) {
            try {
                File file = FileUtils.getFileFromUri(this, selectedImageUri, ".jpg");
                String mimeType = getContentResolver().getType(selectedImageUri);
                RequestBody mediaBody = RequestBody.create(MediaType.parse(mimeType), file);
                image = MultipartBody.Part.createFormData("image", file.getName(), mediaBody);
            } catch (IOException e) {
                Toast.makeText(this, "Lỗi xử lý file", Toast.LENGTH_SHORT).show();
                setLoading(false);
                return;
            }
        }

        apiService.addRecipe(title, description, ingredientParts, stepParts, category,
                        cookingTime, calories, protein, carbs, fat, image)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        setLoading(false);
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(CreateRecipeActivity.this, "Tạo công thức thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CreateRecipeActivity.this, "Lỗi khi tạo công thức!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        setLoading(false);
                        Toast.makeText(CreateRecipeActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInputs() {
        if (titleInput.getText().toString().trim().isEmpty()) {
            titleInput.setError("Tên công thức không được để trống");
            return false;
        }

        if (descriptionInput.getText().toString().trim().isEmpty()) {
            descriptionInput.setError("Mô tả không được để trống");
            return false;
        }

        if (cookingTimeInput.getText().toString().trim().isEmpty()) {
            cookingTimeInput.setError("Thời gian nấu không được để trống");
            return false;
        }

        if (selectedCategory == null) {
            categoryInput.setError("Vui lòng chọn danh mục");
            Toast.makeText(this, "Vui lòng chọn danh mục cho công thức", Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean hasIngredient = ingredients.stream()
                .anyMatch(i -> !i.getIngredient().trim().isEmpty() || !i.getQuantity().trim().isEmpty());
        boolean hasStep = steps.stream().anyMatch(s -> !s.trim().isEmpty());

        if (!hasIngredient) {
            Toast.makeText(this, "Vui lòng thêm ít nhất một nguyên liệu", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!hasStep) {
            Toast.makeText(this, "Vui lòng thêm ít nhất một bước thực hiện", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        publishButton.setEnabled(!loading);
    }

    private RequestBody createPlainText(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private void searchIngredient(String query) {
        apiService.searchIngredients(query).enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    List<String> names = new ArrayList<>();
                    for (Ingredient ing : response.body().getIngredients()) {
                        names.add(ing.getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            CreateRecipeActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            names
                    );
                }
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                Log.e("SearchIngredient", "Lỗi API: " + t.getMessage());
            }
        });
    }

    private void showCategoryLoadError(String errorMessage) {
        categoryInput.setEnabled(false);
        categoryInput.setText("");
        categoryInput.setHint("Không thể tải danh mục");
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

}
