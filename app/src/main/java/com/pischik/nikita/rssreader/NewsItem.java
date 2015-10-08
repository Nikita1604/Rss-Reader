package com.pischik.nikita.rssreader;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//class that represent ORMLite data

@DatabaseTable(tableName = "news")
public class NewsItem {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String title;

    @DatabaseField
    private String pubDate;

    @DatabaseField
    private String imageUrl;

    @DatabaseField
    private String fullNewsUrl;

    public int getId() {
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

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
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
