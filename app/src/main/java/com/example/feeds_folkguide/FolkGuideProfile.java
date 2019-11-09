package com.example.feeds_folkguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FolkGuideProfile extends AppCompatActivity {

    ArrayList<String> imageurls = new ArrayList<>();
    int item;
    ImageView profileMenu;
    ProgressBar profileProgressBar;
    RelativeLayout rellayout2;

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    CollectionReference photos=db.collection("photos");
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthStateListener;
    CollectionReference users;
    CollectionReference user_accounts_settings;
    TextView profileUsername,profileDescription,editprofile,profileWebsite,profiletopUsername,textViewPosts,textViewFollowers,textViewFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GridView gridView=findViewById(R.id.gridView);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folk_guide_profile);
        profileUsername=findViewById(R.id.profile_username);
        profiletopUsername=findViewById(R.id.profiletop_username);
        profileWebsite=findViewById(R.id.profile_website);
        editprofile=findViewById(R.id.textView_editprofile);
        profileDescription=findViewById(R.id.profile_description);
        profileProgressBar = findViewById(R.id.profile_progressbar);
        textViewFollowers=findViewById(R.id.textView_followers);
        textViewFollowing=findViewById(R.id.textView_following);
        textViewPosts=findViewById(R.id.textView_posts);
        textViewFollowing.setText("Team lead");
        CircleImageView profile_photo = (CircleImageView) findViewById(R.id.profile_photo);
        Picasso.with(this).load("https://cdn.pixabay.com/photo/2014/04/09/17/48/man-320276_960_720.png").centerCrop().fit().into(profile_photo);
        profileProgressBar.setVisibility(View.GONE);

        db.collection("feeds").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int count=0;
                ArrayList<String>urls=new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                {
                    urls.add(queryDocumentSnapshot.get("link").toString());
                    count+=1;
                }
                if(urls.size()>0)
                    Log.d("Folk","List not empty");
                final GridView gridView=findViewById(R.id.gridView);
                GridViewAdapter adapter=new GridViewAdapter(urls,FolkGuideProfile.this);
                gridView.setAdapter(adapter);
                textViewPosts.setText(String.valueOf(count));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Folk",e.toString());

            }
        });
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FolkGuideProfile.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

}
