package com.example.feeds_folkguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        final Intent intent=getIntent();
        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        final  String url=intent.getStringExtra("image");
        ImageView imageView=findViewById(R.id.imageView);
      final  TextView des=findViewById(R.id.description);
       final TextView link=findViewById(R.id.link);
        Picasso.with(this).load(url).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(ShowActivity.this,Statuses.class);
                startActivity(intent1);

            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(ShowActivity.this,WebViewActivity.class);
                intent2.putExtra("link",link.getText().toString());
                startActivity(intent2);

            }
        });
    }
}
