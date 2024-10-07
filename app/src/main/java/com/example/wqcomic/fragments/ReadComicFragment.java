package com.example.wqcomic.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wqcomic.R;
import com.example.wqcomic.activities.MainActivity;
import com.example.wqcomic.adapters.ReadComicAdapter;
import com.example.wqcomic.api.ComicApi;
import com.example.wqcomic.api.RetrofitInstance;
import com.example.wqcomic.databinding.FragmentReadComicBinding;
import com.example.wqcomic.models.Chapter;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReadComicFragment extends Fragment {
    FragmentReadComicBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReadComicBinding.inflate(inflater, container, false);

        // Ẩn BottomAppBar và FAB khi vào fragment
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.findViewById(R.id.bottomAppBar).setVisibility(View.GONE);
            mainActivity.findViewById(R.id.fab).setVisibility(View.GONE);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            String comicId = bundle.getString("comicId");
            String chapterId = bundle.getString("chapterId");
            fetchChapterDetails(comicId, chapterId);
        } else {
            Toast.makeText(getContext(), "Read chapter error", Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }

    private void fetchChapterDetails(String comicId, String chapterId) {
        ComicApi comicApi = RetrofitInstance.getApiServiceComics();
        Call<Chapter> call = comicApi.getChapterDetails(comicId, chapterId);

        call.enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(@NonNull Call<Chapter> call, @NonNull Response<Chapter> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Chapter chapter = response.body();
                    if (chapter.getImages() != null && !chapter.getImages().isEmpty()) {
                        binding.rcvDetailComic.setLayoutManager(new LinearLayoutManager(getContext()));
                        ReadComicAdapter adapter = new ReadComicAdapter(chapter.getImages());
                        binding.rcvDetailComic.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getContext(), "Chapter not found !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Chapter> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Hiện lại BottomAppBar và FAB khi rời fragment
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.findViewById(R.id.bottomAppBar).setVisibility(View.VISIBLE);
            mainActivity.findViewById(R.id.fab).setVisibility(View.VISIBLE);
        }
    }
}