package com.example.feeds_folkguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> comments = new ArrayList<>();
    private HashMap<String, String> users = new HashMap<>();
    private ArrayList<String> ids = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter1(Context mContext, ArrayList<String> comments, HashMap<String, String> users,
                                ArrayList<String> ids){
        this.mContext = mContext;
        this.comments = comments;
        this.users = users;
        this.ids = ids;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
//        Glide.with(mContext)
//                .asBitmap()
//                .load(mImages.get(position))
//                .into(holder.image);
        holder.commentsView.setText(comments.get(position));
        if(users.get(ids.get(position)).equalsIgnoreCase("1")) {
            holder.user.setText("user1");
        }
        else
            holder.user.setText("folkguide");

        holder.parent_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on" + comments.get(position));
                Toast.makeText(mContext, comments.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView commentsView;
        TextView user;
        RelativeLayout parent_layout1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentsView = itemView.findViewById(R.id.commentview);
            user = itemView.findViewById(R.id.username);
            parent_layout1 = itemView.findViewById(R.id.parent_layout1);
        }
    }
}