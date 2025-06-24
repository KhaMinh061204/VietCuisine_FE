package com.example.vietcuisine.data.network;

import com.example.vietcuisine.data.model.ApiResponse;
import com.example.vietcuisine.data.model.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    
    @FormUrlEncoded
    @POST("auth/register")
    Call<ApiResponse<String>> register(
        @Field("name") String name,
        @Field("email") String email,
        @Field("password") String password,
        @Field("gender") String gender,
        @Field("phone") String phone
    );
      @FormUrlEncoded
    @POST("auth/login")
    Call<AuthResponse> login(
        @Field("email") String email,
        @Field("password") String password
    );
    
    @GET("auth/profile")
    Call<ApiResponse<Object>> getProfile(@Header("Authorization") String token);
    
    @POST("auth/logout")
    Call<ApiResponse<String>> logout(@Header("Authorization") String token);
    
    @FormUrlEncoded
    @POST("auth/forgot-password")
    Call<ApiResponse<String>> forgotPassword(@Field("email") String email);
    
    @FormUrlEncoded
    @POST("auth/verify-otp")
    Call<ApiResponse<String>> verifyOTP(
        @Field("email") String email,
        @Field("otp") String otp
    );
    
    @FormUrlEncoded
    @POST("auth/reset-password")
    Call<ApiResponse<String>> resetPassword(
        @Field("email") String email,
        @Field("newPassword") String newPassword
    );
}
