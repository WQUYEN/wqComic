package com.example.wqcomic.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.wqcomic.R;
import com.example.wqcomic.adapters.SearchComicsAdapter;
import com.example.wqcomic.api.ComicApi;
import com.example.wqcomic.api.RetrofitInstance;
import com.example.wqcomic.databinding.FragmentSearchBinding;
import com.example.wqcomic.models.Comic;
import com.example.wqcomic.utils.OnClick.ClickDetailComic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements ClickDetailComic {
    private FragmentSearchBinding binding;
    private SearchComicsAdapter adapter;
    private final List<Comic> comicList = new ArrayList<>(); // Danh sách comics
    private List<Comic> fullComicList = new ArrayList<>(); // Danh sách đầy đủ để tìm kiếm

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        setupRecyclerView();
        fetchComics(); // Lấy danh sách comics

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false; // Không cần thực hiện gì ở đây
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterComics(s); // Tìm kiếm khi văn bản thay đổi
                return true;
            }
        });

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new SearchComicsAdapter(getContext(), comicList, SearchFragment.this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        binding.rcvComicsSearch.setLayoutManager(layoutManager);
        binding.rcvComicsSearch.setAdapter(adapter);
    }

    private void fetchComics() {
        ComicApi comicApi = RetrofitInstance.getApiServiceComics();
        Call<List<Comic>> call = comicApi.getAllComics();
        call.enqueue(new Callback<List<Comic>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Comic>> call, @NonNull Response<List<Comic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    comicList.clear(); // Xóa danh sách cũ
                    comicList.addAll(response.body()); // Thêm danh sách mới
                    fullComicList = new ArrayList<>(comicList); // Lưu lại danh sách đầy đủ
                    adapter.notifyDataSetChanged(); // Cập nhật adapter
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

    private void filterComics(String query) {
        List<Comic> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            // Nếu không có từ khóa tìm kiếm, hiển thị toàn bộ danh sách
            filteredList.addAll(fullComicList); // Sử dụng danh sách đầy đủ
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Comic comic : fullComicList) { // Lọc từ danh sách đầy đủ
                if (comic.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(comic); // Thêm comic vào danh sách nếu chứa từ khóa
                }
            }
        }
        adapter.updateList(filteredList); // Cập nhật danh sách trong adapter
    }

    @Override
    public void clickDetailComic(Comic comic) {
        // Thực hiện hành động khi nhấn vào comic (nếu cần)
        Fragment detailComicFragment = new DetailComicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("comicId", comic.getId());
        detailComicFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, detailComicFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}