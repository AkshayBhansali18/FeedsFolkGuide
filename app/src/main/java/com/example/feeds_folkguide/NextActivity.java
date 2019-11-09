package com.example.feeds_folkguide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class NextActivity extends AppCompatActivity {
    FirebaseFirestore db=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

    }
}
