package com.example.wqcomic.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.wqcomic.R;
import com.example.wqcomic.adapters.ChapterAdapter;
import com.example.wqcomic.api.ComicApi;
import com.example.wqcomic.api.RetrofitInstance;
import com.example.wqcomic.databinding.FragmentDetailComicBinding;
import com.example.wqcomic.models.Chapter;
import com.example.wqcomic.models.Comic;
import com.example.wqcomic.models.FavoriteComic;
import com.example.wqcomic.utils.Loading;
import com.example.wqcomic.utils.OnClick.ClickDetailChapter;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailComicFragment extends Fragment implements ClickDetailChapter {
    private FragmentDetailComicBinding binding;
    private Loading loading;
    private ChapterAdapter chapterAdapter;
    private boolean isFollow;
    private boolean isExpanded = false;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailComicBinding.inflate(inflater, container, false);

        // Khởi tạo Loading với inflater và container
        loading = new Loading(inflater, container);
        container.addView(loading.getView()); // Thêm layout loading vào container

        // Hiển thị ProgressBar
        loading.show();

        // Nhận dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String comicId = bundle.getString("comicId");
            String favoriteComicId = bundle.getString("favoriteComicId");

            // Kiểm tra và gọi fetchComicDetail
            if (comicId != null) {
                // Gọi fetchComicDetail với delay 2 giây
                new Handler().postDelayed(() -> {
                    fetchComicDetail(comicId);
                    fetchChapters(comicId);// Gọi với comicId
                    checkFavoriteStatus(comicId);
                    setupFollowButton(comicId);
                }, 1000); // Delay 2000ms = 2 giây
            } else if (favoriteComicId != null) {
                new Handler().postDelayed(() -> {
                    fetchComicDetail(favoriteComicId);
                    fetchChapters(favoriteComicId);
                    checkFavoriteStatus(favoriteComicId);
                    setupFollowButton(favoriteComicId);
                        // Gọi với favoriteComicId
                }, 1000);
            } else {
                Toast.makeText(getContext(), "Không có ID truyện tranh", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getContext(), "Error: Bundle is null", Toast.LENGTH_SHORT).show();
        }
        setupReadNowButton();
        setupReadMoreButton();
        return binding.getRoot();
    }
    private void checkFavoriteStatus(String comicId) {
        // Khởi tạo UserManager
//        UserManager userManager = new UserManager(requireActivity());
//        String userId = userManager.getUserId(); // Lấy ID người dùng
        String userId = "66ebaa43165093890bdc518a";

        // Kiểm tra xem userId có hợp lệ không
        if (userId == null) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập để theo dõi truyện", Toast.LENGTH_SHORT).show();
            return;
        }
        FavoriteComic favoriteComic = new FavoriteComic(userId, comicId);
        ComicApi comicApi = RetrofitInstance.getApiServiceComics();
        comicApi.checkFavoriteStatus(favoriteComic).enqueue(new Callback<Boolean>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isFavorite = response.body(); // Trạng thái yêu thích
                    Log.d("zzzzzzzzzzzzz", "onResponse: check " +isFavorite);
                    if (isFavorite) {
                        binding.addFavourite.setImageResource(R.drawable.favourite);
                        isFollow = true;// Đã yêu thích
                    } else {
                        binding.addFavourite.setImageResource(R.drawable.unfavorite);
                        isFollow = false;// Chưa yêu thích

                    }
                } else {
                    Toast.makeText(getContext(), "Check favorite failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi kiểm tra trạng thái yêu thích: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setupFollowButton(String comicId) {
        binding.addFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFollow) {
                    addFavoriteComic(comicId);
                } else {
                    removeFavoriteComic(comicId);
                }
            }
        });
    }
    private void addFavoriteComic(String comicId) {
//        // Khởi tạo UserManager
//        UserManager userManager = new UserManager(requireActivity());
//        String userId = userManager.getUserId(); // Lấy ID người dùng
        String userId = "66ebaa43165093890bdc518a";

        // Kiểm tra xem userId có hợp lệ không
        if (userId == null) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập để theo dõi truyện", Toast.LENGTH_SHORT).show();
            return;
        }

        FavoriteComic favoriteComic = new FavoriteComic(userId, comicId);
        Log.d("AddFavoriteComic", "Adding Favorite: userId=" + userId + ", comicId=" + comicId);

        ComicApi comicApi = RetrofitInstance.getApiServiceComics();
        comicApi.addFavoriteComic(favoriteComic).enqueue(new Callback<FavoriteComic>() {
            @Override
            public void onResponse(@NonNull Call<FavoriteComic> call, @NonNull Response<FavoriteComic> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã theo dõi", Toast.LENGTH_SHORT).show();
                    checkFavoriteStatus(comicId);
                } else {
                    try {
                        // Lấy thông tin lỗi từ response
                        assert response.errorBody() != null;
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(getContext(), "Error : "+errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FavoriteComic> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: "+ t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void removeFavoriteComic(String comicId) {
        // Khởi tạo UserManager
//        UserManager userManager = new UserManager(requireActivity());
//        String userId = userManager.getUserId(); // Lấy ID người dùng

        // Kiểm tra xem userId có hợp lệ không
        String userId = "66ebaa43165093890bdc518a";

        // Kiểm tra xem userId có hợp lệ không
        if (userId == null) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập để theo dõi truyện", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("RemoveFavoriteComic", "Removing Favorite: userId=" + userId + ", comicId=" + comicId);

        ComicApi comicApi = RetrofitInstance.getApiServiceComics();
        comicApi.removeFavoriteComic(userId, comicId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Huỷ theo dõi", Toast.LENGTH_SHORT).show();
                    checkFavoriteStatus(comicId);
                } else {
                    try {
                        assert response.errorBody() != null;
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(getContext(), "Lỗi: Không thể huỷ theo dõi " +errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi: Không thể huỷ theo dõi " +t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupReadNowButton() {
        binding.btnRead.setOnClickListener(v -> {
            if (chapterAdapter != null && chapterAdapter.getItemCount() > 0) {
                Chapter firstChapter = chapterAdapter.getItem(0); // Lấy chương đầu tiên
                clickChapter(firstChapter); // Gọi phương thức để mở chương
            } else {
                Toast.makeText(getContext(), "Truyện chưa có chương nào", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupReadMoreButton(){
        binding.tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpanded) {
                    // Nếu đã mở rộng, thu gọn lại
                    binding.tvDescription.setMaxLines(2);
                    binding.tvReadMore.setText("Read more");
                } else {
                    // Nếu chưa mở rộng, hiển thị toàn bộ
                    binding.tvDescription.setMaxLines(Integer.MAX_VALUE);
                    binding.tvReadMore.setText("Read less");
                }
                isExpanded = !isExpanded; // Đảo ngược trạng thái
            }
        });
    }
    private void fetchComicDetail(String comicId) {
        ComicApi comicApi = RetrofitInstance.getApiServiceComics();
        Call<Comic> call = comicApi.getComicById(comicId);

        call.enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(@NonNull Call<Comic> call, @NonNull Response<Comic> response) {
                // Ẩn ProgressBar
               // loading.hide();

                if (response.isSuccessful() && response.body() != null) {
                    Comic comic = response.body();
                    // Xử lý comic, ví dụ: cập nhật UI
                    Log.d("ComicDetail", "Title: " + comic.getName());
                    binding.tvTittle.setText(comic.getName());
                    String imageUrl = comic.getCoverImage();
                    Glide.with(binding.coverImg.getContext()).load(imageUrl).into(binding.coverImg);
                    binding.tvDescription.setText(comic.getDescription());
                    if (binding.tvDescription.length()  <=0 || binding.tvDescription.getLineCount() <1){
                        binding.tvReadMore.setVisibility(View.GONE);
                    }
                    // Xử lý genres
                    List<String> genres = comic.getGenres(); // Giả sử genres là List<String>
                    if (genres != null && !genres.isEmpty()) {
                        StringBuilder genreString = new StringBuilder();
                        for (String genre : genres) {
                            genreString.append(genre).append(" . "); // Nối các genre với dấu phẩy
                        }
                        // Xóa dấu phẩy cuối cùng
                        if (genreString.length() > 0) {
                            genreString.setLength(genreString.length() - 2);
                        }
                        binding.tvGenre.setText(genreString.toString()); // Gán vào TextView
                    } else {
                        binding.tvGenre.setText(""); // Nếu không có genre
                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy truyện tranh", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<Comic> call, @NonNull Throwable t) {
                // Ẩn ProgressBar
                //loading.hide();

                Toast.makeText(getContext(), "Lỗi: ở xem chi tiết " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("zzzzzzzzz", "onFailure: comicId" + comicId);
            }
        });
    }
    private void fetchChapters(String comicId) {
        ComicApi comicApi = RetrofitInstance.getApiServiceComics();
        Call<List<Chapter>> call = comicApi.getChapterByComicId(comicId);

        call.enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(@NonNull Call<List<Chapter>> call, @NonNull Response<List<Chapter>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Chapter> chapters = response.body();

                    // Log thông tin của từng chapter
                    for (Chapter chapter : chapters) {
                        Log.d("ChapterData", "ID: " + chapter.getComicId());
                    }

                    // Thiết lập LayoutManager và Adapter cho RecyclerView
                    binding.rcvChapter.setLayoutManager(new LinearLayoutManager(getContext()));
                    chapterAdapter = new ChapterAdapter(chapters,DetailComicFragment.this); // Giả sử bạn có một danh sách chapters
                    binding.rcvChapter.setAdapter(chapterAdapter);

                } else {
                    Toast.makeText(getContext(), "Chapter not found ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Chapter>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void clickChapter(Chapter chapter) {
        Fragment readComicFragment = new ReadComicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("chapterId", chapter.getId());
        bundle.putString("comicId", chapter.getComicId());
        readComicFragment.setArguments(bundle);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, readComicFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}