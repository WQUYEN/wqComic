package com.example.wqcomic.models;

public class FavoriteComic {
    private String userId; // ID của người dùng
    private String comicId; // ID của comic (thay vì đối tượng Comic)
    private Comic comic;
    public FavoriteComic(String userId, String comicId) {
        this.userId = userId;
        this.comicId = comicId; // Chuyển từ comic thành comicId
    }

    // Getter và Setter


    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }
}