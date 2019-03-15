package com.rakhmat.hackernews.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rakhmat.hackernews.Model.NewsRealm;
import com.rakhmat.hackernews.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class FavoriteNewsAdapter extends RealmRecyclerViewAdapter<NewsRealm, FavoriteNewsAdapter.MyViewHolder> {

    public FavoriteNewsAdapter(OrderedRealmCollection<NewsRealm> data) {
        super(data, true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorites, parent, false);

        return new MyViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NewsRealm model = getData().get(position);

        holder.titleNews.setText(model.getTitleNews());
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleNews;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleNews = (TextView) itemView.findViewById(R.id.judul);
        }
    }
}
