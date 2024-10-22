package com.example.wqcomic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wqcomic.api.RetrofitInstance;
import com.example.wqcomic.api.UserApi;
import com.example.wqcomic.databinding.ActivityLoginBinding;
import com.example.wqcomic.models.User;
import com.example.wqcomic.models.request.LoginRequest;
import com.example.wqcomic.models.response.LoginResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userApi = RetrofitInstance.getApiServiceUsers();

        binding.btnLogin.setOnClickListener(view -> {
            String email = Objects.requireNonNull(binding.edEmailLogin.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(binding.edPassword.getEditText()).getText().toString().trim();
            // Kiểm tra nếu email hoặc mật khẩu trống
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra định dạng email
            if (!isValidEmail(email)) {
                Toast.makeText(LoginActivity.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra độ dài mật khẩu
            if (password.length() < 8) {
                Toast.makeText(LoginActivity.this, "Password must be more than 8 characters.", Toast.LENGTH_SHORT).show();
                return;
            }
            LoginRequest loginRequest = new LoginRequest(email, password);
            userApi.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        // Đăng nhập thành công
                        LoginResponse loginResponse = response.body();
                        assert loginResponse != null;

                        // Lưu token
                        String token = loginResponse.getToken();
                        Log.d("token", "onResponse: token is: " + token);
                        saveToken(token); // Lưu token vào SharedPreferences

                        // Tạo đối tượng User mới
                        User user = new User(
                                loginResponse.getUser().getId(),
                                loginResponse.getUser().getName(),
                                loginResponse.getUser().getEmail(),
                                "",
                                loginResponse.getUser().isActive(),
                                loginResponse.getUser().isPremium(),
                                loginResponse.getUser().getAvatar()

                        );
                       // User user = new User(loginResponse.getUser());

                        // Lưu thông tin người dùng vào SharedPreferences
                        saveUser(user);
                        Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // Khi có lỗi từ server
                        try {
                            // Lấy thông báo lỗi từ response.errorBody()
                            assert response.errorBody() != null;
                            String errorBody = response.errorBody().string();

                            // Phân tích và lấy thông điệp từ JSON
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String errorMessage = jsonObject.optString("error", "Invalid email or password.");

                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (IOException | JSONException e) {
                            // Nếu có lỗi phân tích, hiển thị thông báo mặc định
                            Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    // Xử lý lỗi kết nối
                    Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.tvBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    // Phương thức kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(emailPattern);
    }
    // Phương thức lưu thông tin người dùng vào SharedPreferences
    private void saveUser(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Chuyển đổi đối tượng thành chuỗi JSON
        Gson gson = new Gson();
        String userJson = gson.toJson(user);

        editor.putString("user", userJson);
        editor.apply(); // Lưu thay đổi
        // Log thông tin người dùng vừa lưu
        Log.d("zzzzzzzzzzzz", "User saved: " + userJson);
    }
    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("token", token);
        editor.apply(); // Lưu thay đổi
        Log.d("Token", "Token saved: " + token);
    }
}