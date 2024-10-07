package com.example.wqcomic.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wqcomic.R;
import com.example.wqcomic.adapters.AllFavoriteComicsAdapter;
import com.example.wqcomic.adapters.FavoriteComicAdapter;
import com.example.wqcomic.api.ComicApi;
import com.example.wqcomic.api.RetrofitInstance;
import com.example.wqcomic.databinding.FragmentFavoriteBinding;
import com.example.wqcomic.databinding.FragmentHomeBinding;
import com.example.wqcomic.models.Comic;
import com.example.wqcomic.models.FavoriteComic;
import com.example.wqcomic.utils.OnClick.ClickDetailComic;
import com.example.wqcomic.utils.OnClick.ClickDetailFavoriteComic;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteFragment extends Fragment implements ClickDetailFavoriteComic {
    FragmentFavoriteBinding binding;
    AllFavoriteComicsAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater,container,false);
        String userId = "66ebaa43165093890bdc518a";
        fetchFavortieComic(userId);


        return binding.getRoot();
    }

    private void fetchFavortieComic(String userId) {
        ComicApi comicApi = RetrofitInstance.getApiServiceComics(); // Sử dụng API comics
        Call<List<FavoriteComic>> call = comicApi.getFavoriteComicsByUserId(userId); // Gọi API để lấy danh sách yêu thích

        call.enqueue(new Callback<List<FavoriteComic>>() {
            @Override
            public void onResponse(@NonNull Call<List<FavoriteComic>> call, @NonNull Response<List<FavoriteComic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Danh sách FavoriteComic đã được ánh xạ từ JSON
                    List<FavoriteComic> favoriteComics = response.body();

                    // Nếu cần, bạn có thể kiểm tra hoặc xử lý các đối tượng ở đây
                    for (FavoriteComic favorite : favoriteComics) {
                        // Kiểm tra comic
                        if (favorite.getComic() == null) {
                            Log.d("FavoriteComic", "FavoriteComic has null comic");
                        } else {
                            // Nếu cần, bạn có thể ghi log thông tin comic
                            Log.d("FavoriteComic", "Comic ID: " + favorite.getComic().getId());
                        }
                    }

                    // Thiết lập RecyclerView với danh sách FavoriteComic
                    adapter = new AllFavoriteComicsAdapter(getContext(),favoriteComics, FavoriteFragment.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rcvFavorite.setLayoutManager(layoutManager);
                    binding.rcvFavorite.setAdapter(adapter);                } else {
                    Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FavoriteComic>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void clickFavoriteComic(FavoriteComic favoriteComic) {
        // Chuyển sang Detail
        Fragment detailComicFragment = new DetailComicFragment();
        // Tạo Bundle để gửi dữ liệu
        Bundle bundle = new Bundle();
        bundle.putString("favoriteComicId", favoriteComic.getComic().getId()); // trong clickFavoriteComic

        // Đặt bundle vào fragment
        detailComicFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, detailComicFragment); // R.id.frame_layout là ID của FrameLayout trong XML
        transaction.addToBackStack(null); // Thêm vào back stack để có thể quay lại
        transaction.commit();
    }
}