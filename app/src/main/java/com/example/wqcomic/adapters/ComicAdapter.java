package com.example.wqcomic.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wqcomic.databinding.ItemComicBinding;
import com.example.wqcomic.models.Comic;
import com.example.wqcomic.utils.OnClick.ClickDetailComic;

import java.util.List;



public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    private final List<Comic> comics;
    private final ClickDetailComic clickDetailComic; // Thêm field cho interface

    public ComicAdapter(List<Comic> comics, ClickDetailComic clickDetailComic) {
        this.comics = comics;
        this.clickDetailComic = clickDetailComic; // Nhận tham số
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemComicBinding binding = ItemComicBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ComicViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Comic comic = comics.get(position);

        Glide.with(holder.itemView)
                .load(comic.getCoverImage())
                .into(holder.binding.coverImg);

        holder.binding.comicTitle.setText(comic.getName());

//        if (comic.getId().length()){
//            holder.binding.comicId.setText("không lấy được id");
//
//        }else{
//            holder.binding.comicId.setText(comic.getId());
//
//        }
        if (!comic.getChapters().isEmpty()) {
            int chapterIndex = comic.getChapters().size() - 1;
            holder.binding.chapter.setText("Chap " + (chapterIndex + 1));
        } else {
            holder.binding.chapter.setText("0 chapter");
        }

        // Thiết lập sự kiện click cho toàn bộ item
        holder.itemView.setOnClickListener(v -> {
            if (clickDetailComic != null) {
                clickDetailComic.clickDetailComic(comic); // Gọi phương thức trong ClickDetailComic
            }
        });
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    static class ComicViewHolder extends RecyclerView.ViewHolder {
        public final ItemComicBinding binding;

        public ComicViewHolder(ItemComicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}