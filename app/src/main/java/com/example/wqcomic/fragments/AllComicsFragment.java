package com.example.wqcomic.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wqcomic.R;
import com.example.wqcomic.adapters.ComicAdapter;
import com.example.wqcomic.api.ComicApi;
import com.example.wqcomic.api.RetrofitInstance;
import com.example.wqcomic.databinding.FragmentAllComicsBinding;
import com.example.wqcomic.databinding.FragmentHomeBinding;
import com.example.wqcomic.models.Comic;
import com.example.wqcomic.utils.OnClick.ClickDetailComic;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllComicsFragment extends Fragment implements ClickDetailComic {
    FragmentAllComicsBinding binding;
    private ComicAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllComicsBinding.inflate(inflater,container,false);
        fetchNewComics();
        return binding.getRoot();
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
                    adapter = new ComicAdapter(comicList, AllComicsFragment.this);
                    //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

                    binding.rcvComics.setLayoutManager(layoutManager);
                    binding.rcvComics.setAdapter(adapter);
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
}