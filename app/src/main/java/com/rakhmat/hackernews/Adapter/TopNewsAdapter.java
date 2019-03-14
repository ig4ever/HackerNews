package com.rakhmat.hackernews.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rakhmat.hackernews.Model.News;
import com.rakhmat.hackernews.R;

import java.util.List;

public class TopNewsAdapter extends RecyclerView.Adapter<TopNewsAdapter.MyViewHolder> {
    List<News> newsList;
    MyViewHolder mViewHolder;

    public TopNewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        mViewHolder = new MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.idNews.setText("#" + newsList.get(position).getIdNews());
        holder.titleNews.setText(newsList.get(position).getTitleNews());
        holder.authorNews.setText("Author: " + newsList.get(position).getAuthor());
        holder.dateNews.setText(newsList.get(position).getDateNews());
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

        public MyViewHolder(View itemView) {
            super(itemView);
            idNews = (TextView) itemView.findViewById(R.id.id_news);
            titleNews = (TextView) itemView.findViewById(R.id.judul);
            authorNews = (TextView) itemView.findViewById(R.id.penulis);
            dateNews = (TextView) itemView.findViewById(R.id.tanggal_news);
        }
    }
}
