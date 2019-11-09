package com.example.feeds_folkguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Statuses extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private StorageReference imagesStorageRef = mStorageRef.child("images");
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private static final int PICTURE_REQUEST_INTEGER = 1;
    ArrayList<String> usernames=new ArrayList<>();
    Button button2;
    ArrayList<String> dates=new ArrayList<>();
    ListView status_listView;
    ArrayList<String> dp=new ArrayList<>();
    private static final String TAG = "TAG";
    private File mImageFile;
    private Bitmap imageBitmap;

    private String imageFilename;
    private String authorities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statuses);
        status_listView=findViewById(R.id.status_listView);
        ImageView upload_imageView=findViewById(R.id.upload_imageView);
        ImageView profile_imageView=findViewById(R.id.profile_imageView);
        upload_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Statuses.this,UploadFeeds.class);
                startActivity(intent);
            }
        });
        profile_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Statuses.this,FolkGuideProfile.class);
                startActivity(intent);
            }
        });
        authorities = getApplicationContext().getPackageName() + ".fileprovider";
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String currentuser=user.toString();


        ImageButton button = findViewById(R.id.chat);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Statuses.this, ChatActivity.class));
            }
        });

        ListView status_listView=findViewById(R.id.status_listView);
        db.collection("folkguides")
                .document("users")
                .collection("users")
                .document("images")
                .collection("images")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                        {
                            usernames.add(documentSnapshot.get("user").toString());
                            dp.add(documentSnapshot.get("url").toString());
                            dates.add(String.valueOf(documentSnapshot.get("time")));

                        }
                        StatusAdapter adapter=new StatusAdapter(Statuses.this,usernames,dates,dp);
                        populate(usernames,dates,dp);
                        Log.d("retrieve","success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("retrieve","failed");

            }
        });
        CircleImageView image=findViewById(R.id.circleImageView);
        FirebaseFirestore db=FirebaseFirestore.getInstance();


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Statuses.this,UploadActivity.class);
                dispatchTakePictureIntent();
            }
        });

    }

    public void populate(final ArrayList<String> usernames, final ArrayList<String>time, final ArrayList<String> urls1) {

        StatusAdapter adapter=new StatusAdapter(Statuses.this,usernames,dates,urls1);
        status_listView.setAdapter(adapter);
        status_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent resultIntent = new Intent(Statuses.this, ShowActivity.class);
                resultIntent.putExtra("position",position);
                resultIntent.putExtra("image",urls1.get(position));
                startActivity(resultIntent);

                db.collection("folkguides")
                        .document("users")
                        .collection("users")
                        .document("images")
                        .collection("images").whereEqualTo("url",urls1.get(position)).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                                {

                                }
                                usernames.remove(position);
                                time.remove(position);
                                urls1.remove(position);
                                StatusAdapter adapter=new StatusAdapter(Statuses.this,usernames,dates,urls1);
                                status_listView.setAdapter(adapter);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            mImageFile = new File(Statuses.this.getFilesDir(), new Date().getTime() + ".jpg");
            Uri imageFileUri = FileProvider.getUriForFile(this, authorities, mImageFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

            startActivityForResult(takePictureIntent, PICTURE_REQUEST_INTEGER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_REQUEST_INTEGER && resultCode == RESULT_OK) {
            // data will be null cause we are writing to a file
            // https://stackoverflow.com/questions/3879992/how-to-get-bitmap-from-an-uri
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(mImageFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!mImageFile.delete()) {
                Log.d(TAG, "onActivityResult: DELETE FAILED");
                Toast.makeText(this, "File error", Toast.LENGTH_SHORT).show();
            }

            FirebaseUser user = mAuth.getCurrentUser();
            Date date = new Date();

            imageBitmap = bitmap;
            imageFilename = user.getEmail() + "/" + date.getTime() + ".jpg";

            uploadImage();
        }
    }

    public void uploadImage() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        final StorageReference singleImageRef = imagesStorageRef.child(imageFilename);
//            StorageMetadata metadata = new StorageMetadata.Builder()
//                    .setCustomMetadata("angle", "frontal")
//                    .build();
        UploadTask imageUploadTask = singleImageRef.putBytes(data);

        Log.d(TAG, "uploadImage: STARTED");
        
        imageUploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        singleImageRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d(TAG, "onSuccess: DONEEE");
                                        
                                        String downloadURL = uri.toString();
                                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                        updateDatabaseWithDownloadURL(downloadURL,user.toString());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: GET DOWNLOAD URL FAILED");
                                    }
                                });

                        Log.d(TAG, "onSuccess: IMAGES DONE");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: UPLOAD IMAGE " + e.toString());
                    }
                });
    }

    private void updateDatabaseWithDownloadURL(String downloadURL,String user) {
         HashMap<String,Object> map=new HashMap<>();
         map.put("url",downloadURL);
         map.put("user","FolkGuide1");
         map.put("time",FieldValue.serverTimestamp());
        db.collection("folkguides").document("users").collection("users").document("images").collection("images").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Statuses","Update successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Statuses","failed");
            }
        });
    }
}
