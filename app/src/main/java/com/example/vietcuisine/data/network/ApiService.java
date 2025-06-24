package com.example.vietcuisine.data.network;

import com.example.vietcuisine.data.model.*;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    
    // Auth endpoints
    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);
    
    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);
    
    @POST("auth/logout")
    Call<ApiResponse> logout();
    
    @GET("auth/profile")
    Call<UserResponse> getUserProfile();
    
    @PUT("auth/update-profile")
    Call<UserResponse> updateProfile(@Body UpdateProfileRequest request);
    
    @POST("auth/forgot-password")
    Call<ApiResponse> forgotPassword(@Body ForgotPasswordRequest request);
    
    @POST("auth/verify-otp")
    Call<ApiResponse> verifyOtp(@Body VerifyOtpRequest request);
    
    @POST("auth/reset-password")
    Call<ApiResponse> resetPassword(@Body ResetPasswordRequest request);
    
    // Recipe endpoints
    @GET("recipes")
    Call<RecipeResponse> getRecipesInHomepage();
    
    @GET("recipes/all")
    Call<RecipeResponse> getAllRecipes();
    
    @GET("recipes/my")
    Call<RecipeResponse> getMyRecipes();
    
    @GET("recipes/savedRecipes")
    Call<RecipeResponse> getSavedRecipes();
    
    @GET("recipes/category/{categoryId}")
    Call<RecipeResponse> getRecipesByCategory(@Path("categoryId") String categoryId);
    
    @GET("recipes/search")
    Call<RecipeResponse> searchRecipes(@Query("q") String query);
    
    @GET("recipes/{id}")
    Call<RecipeDetailResponse> getRecipeById(@Path("id") String id);
    
    @Multipart
    @POST("recipes/add")
    Call<ApiResponse> addRecipe(
        @Part("title") RequestBody title,
        @Part("description") RequestBody description,
        @Part("ingredients") RequestBody ingredients,
        @Part("steps") RequestBody steps,
        @Part("categoriesId") RequestBody categoryId,
        @Part("cookingTime") RequestBody cookingTime,
        @Part("servings") RequestBody servings,
        @Part("calories") RequestBody calories,
        @Part("protein") RequestBody protein,
        @Part("carbs") RequestBody carbs,
        @Part("fat") RequestBody fat,
        @Part MultipartBody.Part image
    );
    
    @PUT("recipes/{id}")
    Call<ApiResponse> updateRecipe(@Path("id") String id, @Body UpdateRecipeRequest request);
    
    @DELETE("recipes/{id}")
    Call<ApiResponse> deleteRecipe(@Path("id") String id);
    
    @POST("recipes/{id}/toggle-like")
    Call<ApiResponse> toggleLikeRecipe(@Path("id") String id);
    
    @POST("recipes/{id}/toggle-save")
    Call<ApiResponse> toggleSaveRecipe(@Path("id") String id);
    
    @POST("recipes/{id}/comments")
    Call<ApiResponse> addRecipeComment(@Path("id") String id, @Body CommentRequest request);
    
    @DELETE("recipes/{id}/comments/{commentId}")
    Call<ApiResponse> deleteRecipeComment(@Path("id") String recipeId, @Path("commentId") String commentId);
      // Category endpoints
    @GET("categories/all")
    Call<List<Category>> getAllCategories();
    
    // Post endpoints
    @GET("posts")
    Call<PostResponse> getAllPosts();
    
    @GET("posts/my")
    Call<PostResponse> getMyPosts();
    
    @GET("posts/{id}")
    Call<PostDetailResponse> getPostById(@Path("id") String id);
    
    @Multipart
    @POST("posts")
    Call<ApiResponse> createPost(
        @Part("content") RequestBody content,
        @Part MultipartBody.Part image
    );
    
    @Multipart
    @PUT("posts/{id}")
    Call<ApiResponse> updatePost(
        @Path("id") String id,
        @Part("content") RequestBody content,
        @Part MultipartBody.Part image
    );
    
    @DELETE("posts/{id}")
    Call<ApiResponse> deletePost(@Path("id") String id);
    
    // Reel endpoints
    @GET("reels/all")
    Call<ReelResponse> getAllReels();
    
    @Multipart
    @POST("reels/add")
    Call<ApiResponse> addReel(
        @Part("caption") RequestBody caption,
        @Part MultipartBody.Part video
    );
    
    @Multipart
    @PUT("reels/{id}")
    Call<ApiResponse> updateReel(
        @Path("id") String id,
        @Part("caption") RequestBody caption,
        @Part MultipartBody.Part video
    );
    
    @DELETE("reels/{id}")
    Call<ApiResponse> deleteReel(@Path("id") String id);
    
    // Like endpoints
    @POST("likes")
    Call<ApiResponse> toggleLike(@Body LikeRequest request);
    
    @GET("likes")
    Call<LikeResponse> getLikes(@Query("targetId") String targetId, @Query("onModel") String onModel);
    
    // Comment endpoints
    @POST("comments")
    Call<ApiResponse> createComment(@Body CommentRequest request);
    
    @GET("comments")
    Call<CommentResponse> getComments(@Query("targetId") String targetId, @Query("onModel") String onModel);
    
    @DELETE("comments/{id}")
    Call<ApiResponse> deleteComment(@Path("id") String id);
    
    // Ingredient endpoints
    @GET("ingredients/all")
    Call<IngredientResponse> getAllIngredients();
    
    @GET("ingredients/search")
    Call<IngredientResponse> searchIngredients(@Query("q") String query);
    
    @GET("ingredients/{id}")
    Call<IngredientDetailResponse> getIngredientById(@Path("id") String id);
    
    // Ingredient Order endpoints
    @POST("ingredient-orders")
    Call<ApiResponse> createOrder(@Body OrderRequest request);
    
    @GET("ingredient-orders/my")
    Call<OrderResponse> getMyOrders();
    
    @GET("ingredient-orders/{id}")
    Call<OrderDetailResponse> getOrderById(@Path("id") String id);
    
    @POST("ingredient-orders/payment")
    Call<PaymentResponse> processPayment(@Body PaymentRequest request);
    
    // Message endpoints
    @GET("messages/conversations/{userId}")
    Call<ConversationResponse> getConversations(@Path("userId") String userId);
    
    @GET("messages/{userId1}/{userId2}")
    Call<MessageResponse> getMessagesBetweenUsers(@Path("userId1") String userId1, @Path("userId2") String userId2);
    
    // Admin endpoints (if user has admin role)
    @GET("admin/users")
    Call<UserListResponse> getAllUsers();
    
    @PUT("admin/accounts/{id}/status")
    Call<ApiResponse> updateAccountStatus(@Path("id") String id, @Body UpdateStatusRequest request);
    
    @GET("admin/order/all")
    Call<OrderResponse> getAllOrders();
}
