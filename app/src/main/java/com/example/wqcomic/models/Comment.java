package com.example.wqcomic.models;

import java.util.Date;

public class Comment {
    private String id; // ID của comment
    private String content; // Nội dung comment
    private String comicId; // ID của comic mà comment thuộc về
    private String userId; // ID của người dùng đã tạo comment
    private Date createdAt; // Thời gian tạo comment
    private Date updatedAt; // Thời gian cập nhật comment

    // Constructor
    public Comment(String id, String content, String comicId, String userId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.content = content;
        this.comicId = comicId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}