package com.rakhmat.hackernews.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.rakhmat.hackernews.Adapter.FavoriteNewsAdapter;
import com.rakhmat.hackernews.Model.NewsRealm;
import com.rakhmat.hackernews.R;
import com.rakhmat.hackernews.Realm.RealmController;
import com.rakhmat.hackernews.SimpleDividerItems;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FavoriteActivity extends AppCompatActivity {
    private Context context;
    private Activity activity;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        context = this;
        activity = this;

        //Realm Setup
        Realm.init(context);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        Realm realm = Realm.getInstance(configuration);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_favorite);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#528EFF")));
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = view.findViewById(R.id.button_back_activity);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager =
                        (InputMethodManager) context.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });

        recyclerView = findViewById(R.id.rv_favorite_news);
        recyclerView.addItemDecoration(new SimpleDividerItems(this));
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        FavoriteNewsAdapter topNewsAdapter = new FavoriteNewsAdapter(realm.where(NewsRealm.class).findAll());
        recyclerView.setAdapter(topNewsAdapter);
    }
}
