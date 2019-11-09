package com.example.feeds_folkguide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    FirebaseFirestore db=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db.collection("feeds").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<String> name=new ArrayList<>();
                ArrayList<String>likes=new ArrayList<>();
                ArrayList<String> comments=new ArrayList<>();
                ArrayList<String> links=new ArrayList<>();
                ArrayList<String> desc=new ArrayList<>();
                ArrayList<String> time=new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                {
                    name.add(queryDocumentSnapshot.get("Name").toString());
                    likes.add(queryDocumentSnapshot.get("likes").toString());
                    links.add(queryDocumentSnapshot.get("link").toString());
                    desc.add(queryDocumentSnapshot.get("tag").toString());
                    time.add(queryDocumentSnapshot.get("time").toString());
                }
                FeedListAdapter adapter=new FeedListAdapter(ProfileActivity.this,name,likes,links,desc,time);
                ListView listView=findViewById(R.id.feedListView);
                listView.setAdapter(adapter);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
