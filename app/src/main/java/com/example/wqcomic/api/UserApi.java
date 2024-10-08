package com.example.wqcomic.api;

import com.example.wqcomic.models.User;
import com.example.wqcomic.models.request.LoginRequest;
import com.example.wqcomic.models.request.ReadingHistoryRequest;
import com.example.wqcomic.models.request.RegisterRequest;
import com.example.wqcomic.models.response.LoginResponse;
import com.example.wqcomic.models.response.ReadingHistoryResponse;
import com.example.wqcomic.models.response.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @GET("users/list")
    Call<List<User>> getAllUsers();

    @POST("users/add")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("users/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("users/{id}")
    Call<User> getUserById(@Header("Authorization") String token, @Path("id") String id);

    @POST("history/update")
    Call<Void> updateReadingHistory(@Body ReadingHistoryRequest request);
    @GET("history/{userId}")
    Call<List<ReadingHistoryResponse>> getReadingHistory(@Path("userId") String userId);
}