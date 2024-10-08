package com.example.wqcomic.models.request;

public class ReadingHistoryRequest {
    private String userId;
    private String comicId;
    private String chapterId;

    public ReadingHistoryRequest(String userId, String comicId, String chapterId) {
        this.userId = userId;
        this.comicId = comicId;
        this.chapterId = chapterId;
    }

    // Getter và Setter
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

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
}