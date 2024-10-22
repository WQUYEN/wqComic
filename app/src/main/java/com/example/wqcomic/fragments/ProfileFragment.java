package com.example.wqcomic.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.wqcomic.R;
import com.example.wqcomic.activities.LoginActivity;
import com.example.wqcomic.api.RetrofitInstance;
import com.example.wqcomic.api.UserApi;
import com.example.wqcomic.databinding.FragmentProfileBinding;
import com.example.wqcomic.models.User;
import com.example.wqcomic.utils.AuthManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1; // Mã yêu cầu cho chọn hình ảnh
    FragmentProfileBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private AuthManager authManager;
    private Uri imageUri; // URI của hình ảnh đã chọn

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        authManager = new AuthManager(requireActivity());
        String userId = authManager.getUserId(); // Lấy ID người dùng
        String token = authManager.getToken();

        // Kiểm tra xem userId có hợp lệ không
        if (userId == null) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập để theo dõi truyện", Toast.LENGTH_SHORT).show();
        } else {
            fetchUser(userId, token);
        }

        registerResult();
        // Thiết lập sự kiện click cho imgAvatar
        binding.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

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
                    Glide.with(requireContext())
                            .load(user.getAvatar())
                            .placeholder(R.drawable.img01)
                            .error(R.drawable.img01)
                            .into(binding.imgAvatar);
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


    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent); // Gọi ActivityResultLauncher
    }


    private void registerResult() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            updateAvatar(imageUri);
                        } else {
                            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }


//private void updateAvatar(Uri imageUri) {
//    try {
//        // Sử dụng ContentResolver để lấy InputStream từ Uri
//        InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
//        File file = new File(requireContext().getCacheDir(), "avatar.jpg"); // Tạo tệp tạm thời trong bộ nhớ cache
//        FileOutputStream outputStream = new FileOutputStream(file);
//
//        // Đọc từ InputStream và ghi vào FileOutputStream
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = inputStream.read(buffer)) > 0) {
//            outputStream.write(buffer, 0, length);
//        }
//
//        outputStream.close();
//        inputStream.close();
//
//        // Tạo RequestBody từ File
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
//        MultipartBody.Part part = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);
//
//        String userId = authManager.getUserId();
//        String token = authManager.getToken();
//
//        UserApi userApi = RetrofitInstance.getApiServiceUsers();
//        Call<ResponseBody> call = userApi.uploadAvatar(userId, part,"Bearer "+ token);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(getContext(), "Cập nhật avatar thành công", Toast.LENGTH_SHORT).show();
//                    fetchUser(userId, token); // Tải lại thông tin người dùng để cập nhật avatar mới
//                } else {
//                    Toast.makeText(getContext(), "Cập nhật avatar thất bại. Mã lỗi: " + response.code() +" message "+ response.message(), Toast.LENGTH_SHORT).show();                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
//                Toast.makeText(getContext(), "Lỗi: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    } catch (IOException e) {
//        Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//    }
//}
//}
private void updateAvatar(Uri imageUri) {
    try {
        // Sử dụng ContentResolver để lấy InputStream từ Uri
        InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
        if (inputStream == null) {
            Toast.makeText(getContext(), "Không thể mở tệp hình ảnh.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo tệp tạm thời trong bộ nhớ cache
        File file = new File(requireContext().getCacheDir(), "avatar.jpg");
        FileOutputStream outputStream = new FileOutputStream(file);

        // Đọc từ InputStream và ghi vào FileOutputStream
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        inputStream.close();

        // Tạo RequestBody từ File
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);

        // Lấy userId và token
        String userId = authManager.getUserId();
        String token = authManager.getToken();

        // Tạo RequestBody cho userId
        RequestBody userIdRequestBody = RequestBody.create(MediaType.parse("text/plain"), userId);

        // Tạo đối tượng UserApi
        UserApi userApi = RetrofitInstance.getApiServiceUsers();

        // Gửi yêu cầu
        Call<ResponseBody> call = userApi.uploadAvatar(userIdRequestBody, part, "Bearer " +  token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Cập nhật avatar thành công", Toast.LENGTH_SHORT).show();
                    fetchUser(userId, token); // Tải lại thông tin người dùng để cập nhật avatar mới
                } else {
                    Toast.makeText(getContext(), "Cập nhật avatar thất bại. Mã lỗi: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                Toast.makeText(getContext(), "Lỗi: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    } catch (IOException e) {
        Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    }
    private void logout() {
        // Xóa thông tin người dùng và token
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user"); // Xóa thông tin người dùng
        editor.remove("token"); // Xóa token
        editor.apply(); // Lưu thay đổi

        // Quay lại màn hình đăng nhập
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Xóa tất cả activity trước đó
        startActivity(intent);
        requireActivity().finish();
    }
}