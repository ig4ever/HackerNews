package com.rakhmat.hackernews.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rakhmat.hackernews.Model.News;
import com.rakhmat.hackernews.Model.NewsRealm;
import com.rakhmat.hackernews.R;
import com.rakhmat.hackernews.Realm.RealmController;

import java.util.List;

public class TopNewsAdapter extends RecyclerView.Adapter<TopNewsAdapter.MyViewHolder> {
    List<News> newsList;
    MyViewHolder mViewHolder;
    RealmController realmController;

    public TopNewsAdapter(List<News> newsList, RealmController realmController) {
        this.newsList = newsList;
        this.realmController = realmController;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        mViewHolder = new MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.idNews.setText("#" + newsList.get(position).getIdNews());
        holder.titleNews.setText(newsList.get(position).getTitleNews());
        holder.authorNews.setText("Author: " + newsList.get(position).getAuthor());
        holder.dateNews.setText(newsList.get(position).getDateNews());
        if(!newsList.get(position).getTitleNews().equals(realmController.getNews(newsList.get(position).getTitleNews()))){
            holder.buttonFavorite.setImageResource(R.drawable.ic_favorite_border_black_28dp);
        }else {
            holder.buttonFavorite.setImageResource(R.drawable.ic_favorite_red_28dp);
        }

        holder.buttonFavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ImageButton imageButton = view.findViewById(R.id.button_favorite);
                if(newsList.get(position).getTitleNews().equals(realmController.getNews(newsList.get(position).getTitleNews()))){
                    imageButton.setImageResource(R.drawable.ic_favorite_border_black_28dp);
                    realmController.deleteNews(newsList.get(position).getTitleNews());

                }else {
                    imageButton.setImageResource(R.drawable.ic_favorite_red_28dp);
                    NewsRealm modelNews = new NewsRealm();
                    modelNews.setTitleNews(newsList.get(position).getTitleNews());
                    realmController.saveNews(modelNews);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView idNews;
        public TextView titleNews;
        public TextView authorNews;
        public TextView dateNews;
        public ImageButton buttonFavorite;

        public MyViewHolder(View itemView) {
            super(itemView);
            idNews = (TextView) itemView.findViewById(R.id.id_news);
            titleNews = (TextView) itemView.findViewById(R.id.judul);
            authorNews = (TextView) itemView.findViewById(R.id.penulis);
            dateNews = (TextView) itemView.findViewById(R.id.tanggal_news);
            buttonFavorite = (ImageButton) itemView.findViewById(R.id.button_favorite);
        }
    }
}
