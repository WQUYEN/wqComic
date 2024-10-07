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
import com.example.wqcomic.utils.OnClick.ClickDetailComic;

import java.util.ArrayList;
import java.util.List;

public class SearchComicsAdapter extends RecyclerView.Adapter<SearchComicsAdapter.SearchComicsViewHolder> {
    private final Context context;
    private List<Comic> list; // Danh sách hiện tại hiển thị
    private final List<Comic> listFull; // Danh sách đầy đủ để tìm kiếm
    private final ClickDetailComic clickDetailComic;

    public SearchComicsAdapter(Context context, List<Comic> list, ClickDetailComic clickDetailComic) {
        this.context = context;
        this.list = list;
        this.listFull = new ArrayList<>(list); // Lưu danh sách đầy đủ
        this.clickDetailComic = clickDetailComic;
    }

    @NonNull
    @Override
    public SearchComicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemComicBinding binding = ItemComicBinding.inflate(LayoutInflater.from(context), parent, false);
        return new SearchComicsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchComicsViewHolder holder, int position) {
        Comic comic = list.get(position);
        holder.binding.comicTitle.setText(comic.getName());
        Glide.with(holder.itemView)
                .load(comic.getCoverImage())
                .into(holder.binding.coverImg);

        if (!comic.getChapters().isEmpty()) {
            int chapterIndex = comic.getChapters().size() - 1;
            holder.binding.chapter.setText("Chap " + (chapterIndex + 1));
        } else {
            holder.binding.chapter.setText("0 chapter");
        }

        // Thiết lập sự kiện click cho toàn bộ item
        holder.itemView.setOnClickListener(v -> {
            if (clickDetailComic != null) {
                clickDetailComic.clickDetailComic(comic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size(); // Trả về kích thước danh sách hiện tại
    }

    // Hàm cập nhật danh sách
    public void updateList(List<Comic> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged(); // Cập nhật RecyclerView
    }

    // Hàm để lọc danh sách dựa trên từ khóa
    public void filter(String query) {
        if (query.isEmpty()) {
            list.clear();
            list.addAll(listFull); // Khôi phục lại danh sách đầy đủ
        } else {
            List<Comic> filteredList = new ArrayList<>();
            String lowerCaseQuery = query.toLowerCase();
            for (Comic comic : listFull) {
                if (comic.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(comic);
                }
            }
            list.clear();
            list.addAll(filteredList);
        }
        notifyDataSetChanged(); // Cập nhật RecyclerView
    }

    public static class SearchComicsViewHolder extends RecyclerView.ViewHolder {
        public final ItemComicBinding binding;

        public SearchComicsViewHolder(@NonNull ItemComicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}