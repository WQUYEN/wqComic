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
import com.denzcoskun.imageslider.constants.ActionTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.TouchListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.wqcomic.R;
import com.example.wqcomic.adapters.ComicAdapter;
import com.example.wqcomic.adapters.FavoriteComicAdapter;
import com.example.wqcomic.api.ComicApi;
import com.example.wqcomic.api.RetrofitInstance;
import com.example.wqcomic.databinding.FragmentHomeBinding;
import com.example.wqcomic.models.Comic;
import com.example.wqcomic.models.FavoriteComic;
import com.example.wqcomic.utils.AuthManager;
import com.example.wqcomic.utils.OnClick.ClickDetailComic;
import com.example.wqcomic.utils.OnClick.ClickDetailFavoriteComic;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements ClickDetailComic, ClickDetailFavoriteComic {
    FragmentHomeBinding binding;
    ComicAdapter adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);

        AuthManager authManager = new AuthManager(requireActivity());
        String userId = authManager.getUserId(); // Lấy ID người dùng

        // Kiểm tra xem userId có hợp lệ không
        if (userId == null) {
            Toast.makeText(getContext(),("Bạn cần đăng nhập để theo dõi truyện"), Toast.LENGTH_SHORT).show();
        }
// Cài đặt adapter cho RecyclerView
//        YourAdapter adapter = new YourAdapter(yourDataList);
//        recyclerView.setAdapter(adapter);
        setupSlideShow();
        clickSeeAllNewUpdate();
        clickSeeAllFavoriteComic();
        fetchNewComics();
        fetchFavoriteComic(userId);
        return binding.getRoot();
    }


    private void fetchFavoriteComic(String userId) {
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
                    FavoriteComicAdapter adapter1 = new FavoriteComicAdapter(favoriteComics,getContext(),HomeFragment.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    binding.rcvFavorite.setLayoutManager(layoutManager);
                    binding.rcvFavorite.setAdapter(adapter1);
                } else {
                    Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FavoriteComic>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void setUpRecyclerView(List<FavoriteComic> favoriteComics) { // Nhận List<FavoriteComic>
//        ComicAdapter adapter = new ComicAdapter(getContext(), favoriteComics); // Truyền danh sách vào adapter
//        binding.recyclerViewFavoriteComics.setAdapter(adapter);
//    }
    private void clickSeeAllFavoriteComic(){
        binding.lnFavoriteComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang FavoriteFragment
                Fragment favoriteFragment = new FavoriteFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, favoriteFragment); // R.id.frame_layout là ID của FrameLayout trong XML
                transaction.addToBackStack(null); // Thêm vào back stack để có thể quay lại
                transaction.commit();            }
        });
    }
    private void clickSeeAllNewUpdate() {
        binding.lnSeeAllNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang FavoriteFragment
                Fragment allComicsFragment = new AllComicsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, allComicsFragment); // R.id.frame_layout là ID của FrameLayout trong XML
                transaction.addToBackStack(null); // Thêm vào back stack để có thể quay lại
                transaction.commit();            }
        });

    }
    private void fetchNewComics() {
        ComicApi comicApi = RetrofitInstance.getApiServiceComics();
        Call<List<Comic>> call = comicApi.getAllComics();
        call.enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comic>> call, @NonNull Response<List<Comic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Comic comic : response.body()) {
                        Log.d("ComicData", "ID: " + comic.getId());
                    }
                    List<Comic> comicList = response.body();
                    Log.d("zzzzzzzzzzz", "onResponse: "+ comicList);
                    adapter = new ComicAdapter(comicList, HomeFragment.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    binding.rcvNewUpdate.setLayoutManager(layoutManager);
                    binding.rcvNewUpdate.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy truyện tranh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comic>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSlideShow(){
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.img01, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img02, ScaleTypes.FIT));
        binding.imgSlide.setImageList(slideModels,ScaleTypes.FIT);

        binding.imgSlide.setTouchListener(new TouchListener() {
            @Override
            public void onTouched(@NonNull ActionTypes actionTypes) {
                Toast.makeText(getContext(), "Touch banner", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void clickDetailComic(Comic comic) {
        Fragment detailComicFragment = new DetailComicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("comicId", comic.getId());
        detailComicFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, detailComicFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void clickFavoriteComic(FavoriteComic favoriteComic) {
        Log.d("zzzzzz", "clickFavoriteComic: "+favoriteComic.getComic().getId());
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