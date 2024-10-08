package com.example.wqcomic.models.response;

public class ReadingHistoryResponse {
    private String comicId;
    private String chapterId;
    private String lastReadAt;

    // Getter và Setter
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

    public String getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(String lastReadAt) {
        this.lastReadAt = lastReadAt;
    }
}