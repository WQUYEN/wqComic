package com.example.wqcomic.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wqcomic.api.RetrofitInstance;
import com.example.wqcomic.api.UserApi;
import com.example.wqcomic.databinding.FragmentProfileBinding;
import com.example.wqcomic.models.User;
import com.example.wqcomic.utils.AuthManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    private AuthManager authManager;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container, false);
        authManager = new AuthManager(requireActivity());
        String userId = authManager.getUserId(); // Lấy ID người dùng
        String token = authManager.getToken();

        // Kiểm tra xem userId có hợp lệ không
        if (userId == null) {
            Toast.makeText(getContext(),("Bạn cần đăng nhập để theo dõi truyện"), Toast.LENGTH_SHORT).show();
        }else{
            fetchUser(userId, token);
        }
        return binding.getRoot();
    }

    private void fetchUser(String userId, String token) {
        // Gọi API để lấy thông tin người dùng
        UserApi userApi = RetrofitInstance.getApiServiceUsers();

        Log.d("TokenCheck", "Token: " + token); // Ghi log token

        // Gửi token trong header
        Call<User> call = userApi.getUserById("Bearer " + token, userId);
        call.enqueue(new Callback<User>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    binding.tvName.setText("Hello! " + user.getName());
                    binding.tvEmail.setText(user.getEmail());
                    Log.d("Profile", "onResponse: " + user.getName());
                } else {
                    // Ghi log để kiểm tra mã và thông điệp phản hồi
                    Log.d("Profile", "Error: " + response.code() + " - " + response.message());
                    Toast.makeText(getContext(), "Không tìm thấy người dùng. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("Profile", "Error: " + t.getMessage()); // Ghi log lỗi
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}