package com.example.wqcomic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wqcomic.databinding.ItemComicBinding;
import com.example.wqcomic.databinding.ItemFavoriteComicBinding;
import com.example.wqcomic.models.Comic;
import com.example.wqcomic.models.FavoriteComic;
import com.example.wqcomic.utils.OnClick.ClickDetailComic;
import com.example.wqcomic.utils.OnClick.ClickDetailFavoriteComic;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class AllFavoriteComicsAdapter extends RecyclerView.Adapter<AllFavoriteComicsAdapter.ViewHolder> {

    private final List<FavoriteComic> favoriteComics; // Duy trì danh sách FavoriteComic
    private final Context context;
    private ClickDetailFavoriteComic clickDetailFavoriteComic;

    public AllFavoriteComicsAdapter(Context context, List<FavoriteComic> comics, ClickDetailFavoriteComic clickDetailFavoriteComic) {
        this.context = context;
        this.favoriteComics = comics;
        this.clickDetailFavoriteComic = clickDetailFavoriteComic;// Nhận danh sách FavoriteComic
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteComicBinding binding = ItemFavoriteComicBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteComic favoriteComic = favoriteComics.get(position);
        holder.bind(favoriteComic);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemFavoriteComicBinding binding;

        public ViewHolder(ItemFavoriteComicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(FavoriteComic favoriteComic) {
            // Lấy comic từ favoriteComic
            Comic comic = favoriteComic.getComic(); // Giả sử bạn đã cập nhật model FavoriteComic

            if (comic != null) {
                binding.comicTitle.setText(comic.getName()); // Tên comic
                if (!comic.getChapters().isEmpty()) {
                    int chapterIndex = comic.getChapters().size() - 1;
                    binding.chapter.setText("Chap " + (chapterIndex + 1));
                } else {
                    binding.chapter.setText("0 Chapter");
                }
                if (comic.getUpdatedAt() != null){
                    // Định dạng ngày
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = dateFormat.format(comic.getUpdatedAt());

                    // Gán giá trị cho TextView
                    binding.comicUpdateAt.setText(formattedDate);
                }

//                binding.textViewComicYear.setText(String.valueOf(comic.getYear())); // Năm phát hành
//                binding.textViewComicDescription.setText(comic.getDescription()); // Mô tả comic

                // Tải ảnh bìa bằng Glide
                Glide.with(context)
                        .load(comic.getCoverImage())
                        .into(binding.coverImg); // Giả sử bạn có một ImageView cho ảnh bìa

            } else {
//                binding.textViewComicTitle.setText("Unknown Comic");
//                binding.textViewComicAuthor.setText("");
//                binding.textViewComicYear.setText("");
//                binding.textViewComicDescription.setText("");
//                binding.imageViewComicCover.setImageDrawable(null); // Xóa ảnh bìa nếu không có
            }
        }
    }
}