package com.android.bizvoxexam;


import android.graphics.Bitmap;

public class ShotItem {

    private int id;
    private String title;
    private String imageURL;
    private String description;
    private String viewsCount;
    private String commentsCount;
    private String createdAt;
    private Bitmap image;


    public ShotItem(){ }

    public ShotItem(int id, String title, String imageURL, String description, String viewsCount, String commentsCount, String createdAt){
        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.description = description;
        this.viewsCount = viewsCount;
        this.commentsCount = commentsCount;
        this.createdAt = createdAt;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(String viewsCount) {
        this.viewsCount = viewsCount;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
