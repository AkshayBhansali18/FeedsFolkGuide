package com.example.feeds_folkguide;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
        ArrayList<String> imageurls;
        Context context;
        ImageView gridview_imageView;
        ProgressBar gridview_progressbar;

        public GridViewAdapter(ArrayList<String> imageurls, Context context) {
            this.imageurls = imageurls;
            this.context = context;
        }

        @Override
        public int getCount() {
            return imageurls.size();
        }

        @Override
        public Object getItem(int position) {
            return imageurls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
            {
                convertView= LayoutInflater.from(context).inflate(R.layout.gridview_layout,parent,false);
                gridview_imageView=convertView.findViewById(R.id.gridview_imageView);
                gridview_progressbar=convertView.findViewById(R.id.gridview_progressbar);
                if(gridview_imageView==null)
                {
                    gridview_progressbar.setVisibility(View.VISIBLE);
                }
                Picasso.with(context).load(imageurls.get(position)).centerCrop().fit().into(gridview_imageView);
                gridview_progressbar.setVisibility(View.GONE);
            }
            return convertView;

        }
    }
