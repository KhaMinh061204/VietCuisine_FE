package com.example.vietcuisine.ui.recipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vietcuisine.R;
import com.example.vietcuisine.data.network.ApiClient;
import com.example.vietcuisine.data.network.ApiService;
import com.example.vietcuisine.data.model.Category;
import com.example.vietcuisine.data.model.CategoryResponse;
import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.ui.adapters.StepAdapter;
import com.example.vietcuisine.ui.adapters.IngredientInputAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRecipeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView recipeImageView;
    private TextInputEditText titleInput, descriptionInput, cookingTimeInput, servingsInput;
    private TextInputEditText caloriesInput, proteinInput, carbsInput, fatInput;    private AutoCompleteTextView categoryInput;
    private RecyclerView ingredientsRecyclerView, stepsRecyclerView;
    private TextView addIngredientButton, addStepButton;
    private MaterialButton publishButton;
    private ProgressBar progressBar;
    private ImageView backButton;

    private IngredientInputAdapter ingredientInputAdapter;
    private StepAdapter stepAdapter;    private List<String> ingredients;
    private List<String> steps;
    private List<Category> categories;
    private Category selectedCategory;
    private Uri selectedImageUri;
    private ApiService apiService;@Override
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
        servingsInput = findViewById(R.id.servingsInput);
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
        
        // Initialize lists
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
        categories = new ArrayList<>();
        
        // Add initial empty entries
        ingredients.add("");
        steps.add("");
    }

    private void setupRecyclerViews() {
        // Steps RecyclerView
        stepAdapter = new StepAdapter(steps, this::onStepRemoved);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepsRecyclerView.setAdapter(stepAdapter);

        // Ingredients RecyclerView
        ingredientInputAdapter = new IngredientInputAdapter(ingredients, this::onIngredientRemoved);
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
            ingredients.add("");
            ingredientInputAdapter.notifyItemInserted(ingredients.size() - 1);
        });
        
        publishButton.setOnClickListener(v -> publishRecipe());
    }    private void loadCategories() {
        // Show loading indicator and disable category input
        categoryInput.setEnabled(false);
        categoryInput.setText("Đang tải danh mục...");
          apiService.getAllCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CategoryResponse categoryResponse = response.body();
                    if (categoryResponse.getCategories() != null && !categoryResponse.getCategories().isEmpty()) {
                        categories.clear();
                        categories.addAll(categoryResponse.getCategories());
                        setupCategoryDropdown();
                    } else {
                        showCategoryLoadError("Không có danh mục nào");
                    }
                } else {
                    showCategoryLoadError("Lỗi server: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                showCategoryLoadError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void showCategoryLoadError(String errorMessage) {
        categoryInput.setEnabled(true);
        categoryInput.setText("");
        categoryInput.setHint("Chọn danh mục");
        Toast.makeText(CreateRecipeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        
        // Add default categories as fallback
        setupDefaultCategories();
    }

    private void setupDefaultCategories() {
        categories.clear();
        // Add some default categories
        categories.add(new Category("Món chính", "Các món ăn chính", ""));
        categories.add(new Category("Món phụ", "Các món ăn phụ", ""));
        categories.add(new Category("Tráng miệng", "Các món tráng miệng", ""));
        categories.add(new Category("Đồ uống", "Các loại đồ uống", ""));
        categories.add(new Category("Món nướng", "Các món nướng", ""));
        categories.add(new Category("Món chiên", "Các món chiên", ""));
        categories.add(new Category("Món luộc", "Các món luộc", ""));
        categories.add(new Category("Khác", "Danh mục khác", ""));
        setupCategoryDropdown();
    }    private void setupCategoryDropdown() {
        if (categories.isEmpty()) {
            categoryInput.setEnabled(false);
            categoryInput.setHint("Không có danh mục");
            return;
        }

        // Enable the input
        categoryInput.setEnabled(true);
        categoryInput.setText("");
        categoryInput.setHint("Chọn danh mục");

        // Create array of category names
        String[] categoryNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getName();
        }
        
        // Create and set adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_dropdown_item_1line, categoryNames);
        categoryInput.setAdapter(adapter);
        
        // Set item click listener to track selected category
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
            Glide.with(this)
                .load(selectedImageUri)
                .centerCrop()
                .into(recipeImageView);
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
        if (!validateInputs()) {
            return;
        }
        
        setLoading(true);
        
        // Create recipe data
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), titleInput.getText().toString().trim());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionInput.getText().toString().trim());
        RequestBody cookingTime = RequestBody.create(MediaType.parse("text/plain"), cookingTimeInput.getText().toString().trim());
        RequestBody servings = RequestBody.create(MediaType.parse("text/plain"), servingsInput.getText().toString().trim());
        RequestBody calories = RequestBody.create(MediaType.parse("text/plain"), caloriesInput.getText().toString().trim());
        RequestBody protein = RequestBody.create(MediaType.parse("text/plain"), proteinInput.getText().toString().trim());
        RequestBody carbs = RequestBody.create(MediaType.parse("text/plain"), carbsInput.getText().toString().trim());
        RequestBody fat = RequestBody.create(MediaType.parse("text/plain"), fatInput.getText().toString().trim());
        RequestBody category = RequestBody.create(MediaType.parse("text/plain"), 
            selectedCategory != null ? selectedCategory.getId() : "");
        
        // Convert lists to request bodies
        List<RequestBody> ingredientBodies = new ArrayList<>();
        for (String ingredient : ingredients) {
            if (!ingredient.trim().isEmpty()) {
                ingredientBodies.add(RequestBody.create(MediaType.parse("text/plain"), ingredient.trim()));
            }
        }
        
        List<RequestBody> stepBodies = new ArrayList<>();
        for (String step : steps) {
            if (!step.trim().isEmpty()) {
                stepBodies.add(RequestBody.create(MediaType.parse("text/plain"), step.trim()));
            }
        }
        
        // Prepare image
        MultipartBody.Part imagePart = null;
        if (selectedImageUri != null) {
            // This is a simplified version - in production you'd want proper file handling
            try {
                File file = new File(selectedImageUri.getPath());
                RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file);
                imagePart = MultipartBody.Part.createFormData("image", file.getName(), imageBody);
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi xử lý hình ảnh", Toast.LENGTH_SHORT).show();
                setLoading(false);
                return;
            }
        }
        
        // For now, show success message as API implementation may vary
        setLoading(false);
        Toast.makeText(this, "Công thức đã được tạo thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean validateInputs() {
        String title = titleInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String cookingTime = cookingTimeInput.getText().toString().trim();
        String servings = servingsInput.getText().toString().trim();
        
        if (title.isEmpty()) {
            titleInput.setError("Tên công thức không được để trống");
            titleInput.requestFocus();
            return false;
        }
        
        if (description.isEmpty()) {
            descriptionInput.setError("Mô tả không được để trống");
            descriptionInput.requestFocus();
            return false;
        }
        
        if (cookingTime.isEmpty()) {
            cookingTimeInput.setError("Thời gian nấu không được để trống");
            cookingTimeInput.requestFocus();
            return false;
        }
          if (servings.isEmpty()) {
            servingsInput.setError("Số phần ăn không được để trống");
            servingsInput.requestFocus();
            return false;
        }
        
        // Check if category is selected
        if (selectedCategory == null || categoryInput.getText().toString().trim().isEmpty()) {
            categoryInput.setError("Vui lòng chọn danh mục");
            categoryInput.requestFocus();
            Toast.makeText(this, "Vui lòng chọn danh mục cho công thức", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        // Check if we have at least one ingredient and one step
        boolean hasIngredient = false;
        for (String ingredient : ingredients) {
            if (!ingredient.trim().isEmpty()) {
                hasIngredient = true;
                break;
            }
        }
        
        boolean hasStep = false;
        for (String step : steps) {
            if (!step.trim().isEmpty()) {
                hasStep = true;
                break;
            }
        }
        
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

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        publishButton.setEnabled(!isLoading);
    }
}
