package com.pischik.nikita.rssreader;

/* model RSS News. Contain field that represent title, publishing date, image url and full news url */

public class RSSNewsModel {
    private String title;
    private String datePublishing;
    private String imageUrl;
    private String fullNewsUrl;

    public RSSNewsModel(String title, String datePublishing, String imageUrl, String fullNewsUrl) {
        this.title = title;
        this.datePublishing = datePublishing;
        this.imageUrl = imageUrl;
        this.fullNewsUrl = fullNewsUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatePublishing() {
        return datePublishing;
    }

    public void setDatePublishing(String datePublishing) {
        this.datePublishing = datePublishing;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullNewsUrl() {
        return fullNewsUrl;
    }

    public void setFullNewsUrl(String fullNewsUrl) {
        this.fullNewsUrl = fullNewsUrl;
    }
}
