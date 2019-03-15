package com.rakhmat.hackernews.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NewsRealm extends RealmObject {
    @PrimaryKey
    private int id;
    private String titleNews;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleNews() {
        return titleNews;
    }

    public void setTitleNews(String titleNews) {
        this.titleNews = titleNews;
    }
}
