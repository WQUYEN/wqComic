package com.example.wqcomic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wqcomic.api.RetrofitInstance;
import com.example.wqcomic.api.UserApi;
import com.example.wqcomic.databinding.ActivityRegisterBinding;
import com.example.wqcomic.models.request.RegisterRequest;
import com.example.wqcomic.models.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userApi = RetrofitInstance.getApiServiceUsers();

        binding.tvBackRegister.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        binding.btnLoginRegister.setOnClickListener(view -> {
            String name = binding.edNameRegister.getEditText().getText().toString().trim();
            String email = binding.edEmailRegister.getEditText().getText().toString().trim();
            String password = binding.edPasswordRegister.getEditText().getText().toString().trim();

            RegisterRequest registerRequest = new RegisterRequest(name, email, password);
            userApi.register(registerRequest).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        RegisterResponse registerResponse = response.body();
                        // Xử lý đăng ký thành công
                        Toast.makeText(RegisterActivity.this, "Register success !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        // Xử lý lỗi đăng ký
                        Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                    // Xử lý lỗi kết nối
                    Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}