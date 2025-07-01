package com.example.vietcuisine.data.network;

import androidx.annotation.Nullable;

import com.example.vietcuisine.data.model.*;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.Map;

public interface ApiService {
    
    // Auth endpoints
    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);
    
    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);
    
    @POST("auth/logout")
    Call<ApiResponse> logout();
    
    @GET("auth/profile")
    Call<User> getUserProfile();
    
    @PUT("auth/update-profile")
    Call<User> updateProfile(@Body UpdateProfileRequest request);
    
    @POST("auth/forgot-password")
    Call<ApiResponse> forgotPassword(@Body ForgotPasswordRequest request);
    
    @POST("auth/verify-otp")
    Call<ApiResponse> verifyOtp(@Body VerifyOtpRequest request);
    
    @POST("auth/reset-password")
    Call<ApiResponse> resetPassword(@Body ResetPasswordRequest request);
      // Recipe endpoints
    @GET("recipe")
    Call<RecipeResponse> getRecipesInHomepage();
    
    @GET("recipe/all")
    Call<RecipeResponse> getAllRecipes();
    
    @GET("recipe/my")
    Call<RecipeResponse> getMyRecipes();
    
    @GET("recipe/savedRecipes")
    Call<RecipeResponse> getSavedRecipes();
      @GET("recipe/category/{categoryId}")
    Call<RecipeResponse> getRecipesByCategory(@Path("categoryId") String categoryId);
    
    @GET("recipe/search")
    Call<RecipeResponse> searchRecipes(@Query("q") String query);
    
    @GET("recipe/{id}")
    Call<RecipeDetailResponse> getRecipeById(@Path("id") String id);
    
    @Multipart
    @POST("recipe/add")
    Call<ApiResponse> addRecipe(
        @Part("title") RequestBody title,
        @Part("description") RequestBody description,
        @Part List<MultipartBody.Part> ingredients,
        @Part List<MultipartBody.Part> steps,
        @Part("categoriesId") RequestBody categoryId,
        @Part("cookingTime") RequestBody cookingTime,
        @Part("calories") RequestBody calories,
        @Part("protein") RequestBody protein,
        @Part("carbs") RequestBody carbs,
        @Part("fat") RequestBody fat,
        @Part MultipartBody.Part image
    );
    @GET("recipe/{id}/ingredients")
    Call<List<RecipeIngredient>> getIngredientByRecipeId(@Path("id") String id);
    @PUT("recipe/{id}")
    Call<ApiResponse> updateRecipe(@Path("id") String id, @Body UpdateRecipeRequest request);
    
    @DELETE("recipe/{id}")
    Call<ApiResponse> deleteRecipe(@Path("id") String id);
    
    @POST("recipe/{id}/toggle-like")
    Call<ApiResponse> toggleLikeRecipe(@Path("id") String id);
    
    @POST("recipe/{id}/toggle-save")
    Call<ApiResponse> toggleSaveRecipe(@Path("id") String id);
    
    @POST("recipe/{id}/comments")
    Call<ApiResponse> addRecipeComment(@Path("id") String id, @Body CommentRequest request);
    
    @DELETE("recipe/{id}/comments/{commentId}")
    Call<ApiResponse> deleteRecipeComment(@Path("id") String recipeId, @Path("commentId") String commentId);

    // Category endpoints
    @GET("category/all")
    Call<CategoryResponse> getAllCategories();

    // Post endpoints
    @GET("posts")
    Call<List<Post>> getAllPosts();
    
    @GET("posts/my")
    Call<PostResponse> getMyPosts();
    
    @GET("posts/{id}")
    Call<PostDetailResponse> getPostById(@Path("id") String id);
    
    @Multipart
    @POST("posts")
    Call<PostDetailResponse> createPost(
        @Part("caption") RequestBody caption,
        @Part("recipeId") @Nullable RequestBody recipeId,
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
    @GET("reel/all")
    Call<ReelResponse> getAllReels();

    @Multipart
    @POST("reel/add")
    Call<ApiResponse> addReel(
            @Part("caption") RequestBody caption,
            @Part MultipartBody.Part video
    );

    @Multipart
    @PUT("reel/{id}")
    Call<ApiResponse> updateReel(
        @Path("id") String id,
        @Part("caption") RequestBody caption,
        @Part MultipartBody.Part video
    );
    
    @DELETE("reel/{id}")
    Call<ApiResponse> deleteReel(@Path("id") String id);
    
    // Like endpoints
    @POST("like")
    Call<ApiResponse> toggleLike(
            @Body LikeRequest request);
    
    @GET("like")
    Call<LikeResponse> getLikes(@Query("targetId") String targetId, @Query("onModel") String onModel);
    
    // Comment endpoints
    @POST("comment")
    Call<ApiResponse> createComment(@Body CommentRequest request);

    @GET("/comment")
    Call<List<Comment>> getComments(@Query("targetId") String targetId, @Query("onModel") String onModel);

    @DELETE("comment/{id}")
    Call<ApiResponse> deleteComment(@Path("id") String id);
    @POST("report")
    Call<ApiResponse> reportComment(@Body ReportRequest reportRequest);
    @GET("comment")
    Call<List<Comment>> getCommentsByTarget(
            @Query("targetId") String targetId,
            @Query("onModel") String onModel
    );
    @GET("comment/limit")
    Call<List<Comment>> getCommentsByTargetLimit(
            @Query("targetId") String targetId,
            @Query("onModel") String onModel
    );

    // Ingredient endpoints
    @GET("ingredient/all")
    Call<IngredientResponse> getAllIngredients();
    
    @GET("ingredient/search")
    Call<IngredientResponse> searchIngredients(@Query("keyword") String query);
    
    @GET("ingredient/{id}")
    Call<IngredientDetailResponse> getIngredientById(@Path("id") String id);
      // Ingredient Order endpoints
    @POST("order")
    Call<OrderListResponse> createOrder(@Body OrderRequest orderRequest);
    
    @GET("order/my")
    Call<OrderListResponse> getMyOrders();
    
    @GET("order/{id}")
    Call<OrderDetailResponse> getOrderById(@Path("id") String id);

    @POST("order/payment")
    Call<PaymentResponse> processPayment(@Body Map<String, String> body);
    @GET("order/status/{status}")
    Call<OrderListResponse> getOrdersByStatus(@Path("status") String status);

    // Message endpoints
    @GET("messages/conversations/{userId}")
    Call<List<MessageUser>> getConversations(@Path("userId") String userId);
    
    @GET("messages/{userId1}/{userId2}")
    Call<List<Message>> getMessagesBetweenUsers(@Path("userId1") String userId1, @Path("userId2") String userId2, @Query("page") int page);
    
    // Admin endpoints (if user has admin role)
    @GET("admin/users")
    Call<UserListResponse> getAllUsers();
    
    @PUT("admin/accounts/{id}/status")
    Call<ApiResponse> updateAccountStatus(@Path("id") String id, @Body UpdateStatusRequest request);
    
    @GET("admin/order/all")
    Call<OrderListResponse> getAllOrders();
}
