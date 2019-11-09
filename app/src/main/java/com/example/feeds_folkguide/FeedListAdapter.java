package com.example.feeds_folkguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedListAdapter extends BaseAdapter {
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String>like=new ArrayList<>();
    ArrayList<String> comments=new ArrayList<>();
    ArrayList<String> links=new ArrayList<>();
    ArrayList<String> desc=new ArrayList<>();
    ArrayList<String> times=new ArrayList<>();
    Context context;
    public FeedListAdapter(Context context,ArrayList<String> name, ArrayList<String> likes, ArrayList<String> links, ArrayList<String> desc, ArrayList<String> time) {
        this.name = name;
        this.like = likes;
        this.links = links;
        this.desc = desc;
        this.times = time;
        this.context=context;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = LayoutInflater.from(context).inflate(R.layout.feed_layout, parent, false);
        ImageView dp=convertView.findViewById(R.id.image);
        TextView title = (TextView)convertView.findViewById(R.id.Title);
        TextView time=convertView.findViewById(R.id.Time);
        TextView likes=convertView.findViewById(R.id.likes);
        TextView tags=convertView.findViewById(R.id.tags);
        title.setText("Daily Quotes");
        time.setText(times.get(position));
        likes.setText("Likes "+like.get(position));
        tags.setText(desc.get(position));
        Picasso.with(context).load(links.get(position)).into(dp);
        return convertView;
    }
}
