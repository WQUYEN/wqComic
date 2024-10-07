package com.example.wqcomic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wqcomic.databinding.ItemComicBinding;
import com.example.wqcomic.models.Comic;
import com.example.wqcomic.models.FavoriteComic;
import com.example.wqcomic.utils.OnClick.ClickDetailComic;
import com.example.wqcomic.utils.OnClick.ClickDetailFavoriteComic;

import java.util.List;


public class FavoriteComicAdapter extends RecyclerView.Adapter<FavoriteComicAdapter.ViewHolder> {

    private final List<FavoriteComic> favoriteComics; // Duy trì danh sách FavoriteComic
    private final Context context;
    private final ClickDetailFavoriteComic clickDetailFavoriteComic;

    public FavoriteComicAdapter(List<FavoriteComic> favoriteComics, Context context, ClickDetailFavoriteComic clickDetailFavoriteComic) {
        this.favoriteComics = favoriteComics;
        this.context = context;
        this.clickDetailFavoriteComic = clickDetailFavoriteComic;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemComicBinding binding = ItemComicBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteComic favoriteComic = favoriteComics.get(position);
        // Lấy comic từ favoriteComic
        if (favoriteComic != null) {
           holder.binding.comicTitle.setText(favoriteComic.getComic().getName()); // Tên comic
            if (!favoriteComic.getComic().getChapters().isEmpty()) {
                int chapterIndex = favoriteComic.getComic().getChapters().size() - 1;
                holder.binding.chapter.setText("Chap " + (chapterIndex + 1));
            } else {
                holder.binding.chapter.setText("0 Chapter");
            }
//                binding.textViewComicYear.setText(String.valueOf(comic.getYear())); // Năm phát hành
//                binding.textViewComicDescription.setText(comic.getDescription()); // Mô tả comic

            // Tải ảnh bìa bằng Glide
            Glide.with(context)
                    .load(favoriteComic.getComic().getCoverImage())
                    .into(holder.binding.coverImg); // Giả sử bạn có một ImageView cho ảnh bìa

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDetailFavoriteComic.clickFavoriteComic(favoriteComic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteComics.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemComicBinding binding;

        public ViewHolder(ItemComicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}