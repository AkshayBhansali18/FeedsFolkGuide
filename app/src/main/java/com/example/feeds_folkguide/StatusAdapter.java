package com.example.feeds_folkguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends BaseAdapter {
    ArrayList<String> username;
    ArrayList<String> uploadDate;
    ArrayList<String> images;
    Context context;
    public StatusAdapter(Context context, ArrayList<String> username, ArrayList<String> uploadDate, ArrayList<String> images) {
        this.username=username;
        this.uploadDate=uploadDate;
        this.context=context;
        this.images=images;


    }

    @Override
    public int getCount() {
        return username.size();
    }

    @Override
    public Object getItem(int position) {
        return username.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = LayoutInflater.from(context).inflate(R.layout.status_layout, parent, false);
        CircleImageView dp=convertView.findViewById(R.id.dp);
        TextView date_textView = (TextView)convertView.findViewById(R.id.time_textView);
        TextView username_textView=convertView.findViewById(R.id.username_textView);
        date_textView.setText(username.get(position));
        Picasso.with(context).load(images.get(position)).into(dp);
        username_textView.setText(uploadDate.get(position));
        return convertView;
    }
}
