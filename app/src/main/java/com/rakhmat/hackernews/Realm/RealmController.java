package com.rakhmat.hackernews.Realm;

import android.util.Log;

import com.rakhmat.hackernews.Model.NewsRealm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmController {
    Realm realm;

    public RealmController(Realm realm) {
        this.realm = realm;
    }

    //menyimpan data news
    public void saveNews(final NewsRealm newsModel){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm != null){
                    Log.e("Created", "Database was created");
                    Number currentIdNum = realm.where(NewsRealm.class).max("id");
                    int nextId;
                    if (currentIdNum == null){
                        nextId = 1;
                    }else {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    newsModel.setId(nextId);
                    realm.copyToRealm(newsModel);
                }else {
                    Log.e("info", "execute : Database not exist");
                }
            }
        });
    }

    //mengambil data news
    public String getNews(final String titleNews){
        RealmResults<NewsRealm> results = realm.where(NewsRealm.class).equalTo("titleNews", titleNews).findAll();
        if (results.size() > 0)
            return results.get(0).getTitleNews();
        else
            return "";
    }

    // untuk menghapus data news
    public void deleteNews(final String titleNews){
        final RealmResults<NewsRealm> model = realm.where(NewsRealm.class).equalTo("titleNews", titleNews).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteAllFromRealm();
            }
        });
    }
}
