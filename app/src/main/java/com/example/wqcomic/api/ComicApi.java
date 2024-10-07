package com.example.wqcomic.api;

import com.example.wqcomic.models.Chapter;
import com.example.wqcomic.models.Comic;
import com.example.wqcomic.models.Comment;
import com.example.wqcomic.models.FavoriteComic;
import com.example.wqcomic.models.Genre;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ComicApi {

    //Genre API
    @GET("genres/list")
    Call<List<Genre>> getAllGenres();
    @GET("genres/{id}")
    Call<Genre> getGenreById(@Path("id") String genreId);
    //Comics API
    @GET("comics/list")
    Call<List<Comic>> getAllComics();

    @GET("comics/detail/{id}")
    Call<Comic> getComicById(@Path("id") String id);
    @GET("chapter/{id}")
    Call<List<Chapter>> getChapterByComicId(@Path("id") String id);
    @GET("chapter/{comicId}/read/{chapterId}")
    Call<Chapter> getChapterDetails(@Path("comicId") String comicId,
                                    @Path("chapterId") String chapterId);

    // Thêm truyện yêu thích
    @POST("favorite/add")
    Call<FavoriteComic> addFavoriteComic(@Body FavoriteComic favoriteComic);
    // Lấy tất cả truyện yêu thích của một user
    @GET("favorite/{userId}")
    Call<List<FavoriteComic>> getFavoriteComicsByUserId(@Path("userId") String userId);
    // Xóa truyện yêu thích
    @DELETE("favorite/remove") // Đường dẫn phù hợp với API của bạn
    Call<Void> removeFavoriteComic(@Query("userId") String userId, @Query("comicId") String comicId);
    //Check follow status
    @POST("favorite/check")
    Call<Boolean> checkFavoriteStatus(@Body FavoriteComic favoriteComic);
    //Post comment
    @POST("comments/create")
    Call<Comment> createComment(@Body Comment comment);
    //Get all comment
    @GET("comments/comic/{comicId}")
    Call<List<Comment>> getCommentsByComicId(@Path("comicId") String comicId);
    //Edit comment
    @PUT("comments/{commentId}")
    Call<Comment> editComment(@Path("commentId") String commentId, @Body Comment comment);
    //Delete comment
    @DELETE("comments/{commentId}")
    Call<Void> deleteComment(@Path("commentId") String commentId, @Body String userId);
}


