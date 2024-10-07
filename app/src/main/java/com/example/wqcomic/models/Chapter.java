    package com.example.wqcomic.models;

    import com.google.gson.annotations.SerializedName;

    import java.io.Serializable;
    import java.util.Date;
    import java.util.List;

    public class Chapter implements Serializable {
        @SerializedName("_id")
        private String id; // Trường ID
        private String title; // Tiêu đề chapter
        private int chapterNumber; // Số chương
        private String content; // Nội dung chapter
        @SerializedName("images")
        private List<String> images; // Đường dẫn ảnh (sửa đổi thành List<String>)
        @SerializedName("comic")
        private String comicId; // ID của comic mà chapter thuộc về
        private Date createdAt; // Thời gian tạo
        private Date updatedAt; // Thời gian cập nhật

        // Constructor
        public Chapter() {
        }

        // Getters và Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getChapterNumber() {
            return chapterNumber;
        }

        public void setChapterNumber(int chapterNumber) {
            this.chapterNumber = chapterNumber;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getComicId() {
            return comicId;
        }

        public void setComicId(String comicId) {
            this.comicId = comicId;
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