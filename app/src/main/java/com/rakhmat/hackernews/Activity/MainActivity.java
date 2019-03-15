package com.rakhmat.hackernews.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.gson.JsonObject;
import com.rakhmat.hackernews.Adapter.TopNewsAdapter;
import com.rakhmat.hackernews.Model.News;
import com.rakhmat.hackernews.R;
import com.rakhmat.hackernews.Realm.RealmController;
import com.rakhmat.hackernews.SimpleDividerItems;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Context context;
    private boolean isScrolling;
    private LinearLayoutManager linearLayoutManager;
    private int currentItems;
    private int totalItems;
    private int scrollOutItems;
    private ProgressBar progressBar;
    private TopNewsAdapter topNewsAdapter;
    private int counter;
    private int totalPerPage;
    private RealmController realmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //Realm Setup
        Realm.init(context);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        Realm realm = Realm.getInstance(configuration);
        realmController = new RealmController(realm);

        recyclerView = findViewById(R.id.rv_top_news);
        recyclerView.addItemDecoration(new SimpleDividerItems(this));
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        progressBar = findViewById(R.id.progress_bar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#528EFF")));
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = view.findViewById(R.id.button_favorite_activity);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });

        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();

        totalPerPage = 20; //total item per page

        try {
            String jsonString = new GetJson().AsString("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < totalPerPage; i++){
                JsonObject jsonObject = new GetJson().AsJSONObject("https://hacker-news.firebaseio.com/v0/item/" + jsonArray.getString(i) + ".json?print=pretty");
                News newsObject = new News();
                newsObject.setIdNews(jsonObject.get("id").getAsString());
                newsObject.setTitleNews(jsonObject.get("title").getAsString());
                newsObject.setAuthor(jsonObject.get("by").getAsString());
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(jsonObject.get("time").getAsInt() * 1000L);
                String formattedDate = DateFormat.format("MMMM dd, yyyy", cal).toString();
                newsObject.setDateNews(formattedDate);

                newsList.add(newsObject);
                counter += 1;
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pd.isShowing()){
                        pd.dismiss();
                    }
                }
            }, 2000);

            recyclerView.setLayoutManager(linearLayoutManager);
            topNewsAdapter = new TopNewsAdapter(newsList, realmController);
            recyclerView.setAdapter(topNewsAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems)){
                    isScrolling = false;
                    fetchMoreData();
                }
            }
        });
    }

    private void fetchMoreData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String jsonString = null;
                try {
                    jsonString = new GetJson().AsString("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
                    JSONArray jsonArray = new JSONArray(jsonString);

                    for (int i = 0; i < totalPerPage; i++){
                        JsonObject jsonObject = new GetJson().AsJSONObject("https://hacker-news.firebaseio.com/v0/item/" + jsonArray.getString(counter) + ".json?print=pretty");
                        News newsObject = new News();
                        newsObject.setIdNews(jsonObject.get("id").getAsString());
                        newsObject.setTitleNews(jsonObject.get("title").getAsString());
                        newsObject.setAuthor(jsonObject.get("by").getAsString());
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(jsonObject.get("time").getAsInt() * 1000L);
                        String formattedDate = DateFormat.format("MMMM dd, yyyy", cal).toString();
                        newsObject.setDateNews(formattedDate);

                        newsList.add(newsObject);
                        counter += 1;
                    }

                    topNewsAdapter.notifyDataSetChanged();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.GONE);
            }
        }, 2000);
    }
}
