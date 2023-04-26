package com.example.dailynews_adminpanel;

public class Model {
    String date, title, content, imageUrl;

    public Model() {
    }

    public Model(String date, String title, String content, String imageUrl) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setUri(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
