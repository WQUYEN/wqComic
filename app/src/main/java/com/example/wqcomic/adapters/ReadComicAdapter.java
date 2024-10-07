package com.example.wqcomic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wqcomic.databinding.ItemReadComicBinding;
import com.example.wqcomic.models.Chapter;

import java.util.List;

public class ReadComicAdapter extends RecyclerView.Adapter<ReadComicAdapter.ReadComicViewHolder> {
    private final List<String> images;

    public ReadComicAdapter(List<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ReadComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReadComicBinding binding = ItemReadComicBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReadComicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadComicViewHolder holder, int position) {
        String imageUrl = images.get(position);
        Glide.with(holder.binding.imgChapter.getContext()).load(imageUrl).into(holder.binding.imgChapter);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ReadComicViewHolder extends RecyclerView.ViewHolder {
        public final ItemReadComicBinding binding;
        public ReadComicViewHolder(@NonNull ItemReadComicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
