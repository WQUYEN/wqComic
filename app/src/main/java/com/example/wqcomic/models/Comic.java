package com.example.wqcomic.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Comic implements Serializable {
    @SerializedName("_id")
    private String id; // Trường ID
    private String name;
    private String author;
    private int year;
    private String description;
    private List<String> genres; // Danh sách ID thể loại
    private List<String> chapters; // Danh sách ID chapters
    private int readCount;
    private String coverImage;
    @SerializedName("createdAt") // Để ánh xạ với trường createdAt từ server
    private Date createdAt; // Trường ngày tạo

    @SerializedName("updatedAt") // Để ánh xạ với trường updatedAt từ server
    private Date updatedAt; // Trường ngày cập nhật
    public Comic(String id, String name, String author, int year, String description, List<String> genres, List<String> chapters, int readCount, String coverImage) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
        this.description = description;
        this.genres = genres;
        this.chapters = chapters;
        this.readCount = readCount;
        this.coverImage = coverImage;
    }

    // Constructor
    public Comic() {
    }


    // Getters and Setters

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getChapters() {
        return chapters;
    }

    public void setChapters(List<String> chapters) {
        this.chapters = chapters;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}
