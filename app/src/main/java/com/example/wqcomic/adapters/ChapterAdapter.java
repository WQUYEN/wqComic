package com.example.wqcomic.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wqcomic.databinding.ItemChapterBinding;
import com.example.wqcomic.models.Chapter;
import com.example.wqcomic.utils.OnClick.ClickDetailChapter;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private final List<Chapter> chapters;

    private final ClickDetailChapter clickDetailChapter;

    public ChapterAdapter(List<Chapter> chapters, ClickDetailChapter clickDetailChapter) {
        this.chapters = chapters;
        this.clickDetailChapter = clickDetailChapter;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChapterBinding binding = ItemChapterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChapterViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapters.get(position);
        holder.binding.tvChapterNumber.setText("Chapter "+chapter.getChapterNumber());
            // Định dạng ngày
        if (chapter.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dateStr = sdf.format(chapter.getCreatedAt());
            holder.binding.tvUpdateAT.setText(dateStr);
        } else {
            holder.binding.tvUpdateAT.setText("Không có thông tin");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickDetailChapter != null){
                    clickDetailChapter.clickChapter(chapter);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }
    public Chapter getItem(int position) {
        return chapters.get(position);
    }

    static class ChapterViewHolder extends RecyclerView.ViewHolder {
        public final ItemChapterBinding binding;

        public ChapterViewHolder(ItemChapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}