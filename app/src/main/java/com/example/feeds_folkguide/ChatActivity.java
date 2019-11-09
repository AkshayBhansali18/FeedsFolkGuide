package com.example.feeds_folkguide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinkedHashSet<String> chat = new LinkedHashSet<>();
    private HashMap<String, String> users = new HashMap<>();
    private LinkedHashSet<String> ids = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Log.d("TAG", "onCreate: Entered chat");

        get_chat();

        ImageButton button = findViewById(R.id.commentsend);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = findViewById(R.id.new_comment);
                String st = et.getText().toString();

                Map<String, Object> newchat = new HashMap<>();
                newchat.put("folkguide", st);

                db.collection("chats")
                        .document()
                        .set(newchat, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(ChatActivity.this, "comment uploaded",
//                                        Toast.LENGTH_SHORT).show();
                                et.setText("");
                                get_chat();
                            }
                        });
            }
        });

    }

    public void get_chat(){
        Log.d("TAG", "get_chat: getting chats");
        db.collection("chats")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Log.d("TAG", "onSuccess: " + documentSnapshot.getId());
                            try {
                                chat.add(documentSnapshot.get("1").toString());
                                ids.add(documentSnapshot.getId());
                                users.put(documentSnapshot.getId(), "1");
                            }catch (Exception e) {
                                Log.d("TAG", "onSuccess: Not 1");
                            }
                            try {
                                chat.add(documentSnapshot.get("folkguide").toString());
                                ids.add(documentSnapshot.getId());
                                users.put(documentSnapshot.getId(), "folkguide");
                            }catch (Exception e) {
                                Log.d("TAG", "onSuccess: Not Folkguide");
                            }
                        }
                        initRecylerView3();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void initRecylerView3(){
        Log.d("TAG", "initRecylerView: HERE");

        RecyclerView recyclerView = findViewById(R.id.recycler_view1);
        RecyclerViewAdapter1 adapter = new RecyclerViewAdapter1(this, new ArrayList<String>(chat),
                users, new ArrayList<String>(ids));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
    }
}