package com.example.feeds_folkguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UploadFeeds extends AppCompatActivity {
    Button camera, gallery, upload;
    EditText desc, link_editText;
    int PICTURE_REQUEST_INTEGER = 1;
    int GALLERY_REQUEST = 1;
    String global_url;
    private File mImageFile;
    private Bitmap imageBitmap;
    ConstraintLayout constraintLayout;
    private String imageFilename;
    HashMap<String, Object> map = new HashMap<>();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    Button vp_button;
    private StorageReference imagesStorageRef = mStorageRef.child("images");
    private String authorities;
    private static final String TAG = "UploadFeeds";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
     ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_feeds);
        gallery = findViewById(R.id.gallery_button);
        upload = findViewById(R.id.upload_button);
        link_editText = findViewById(R.id.link_edittext);
        desc = findViewById(R.id.desc_editText);
        vp_button=findViewById(R.id.viewpager_button);
        vp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link=link_editText.getText().toString();
                String des=desc.getText().toString();
                Intent intent=new Intent(UploadFeeds.this,WebViewActivity.class);
                intent.putExtra("link",link);
                intent.putExtra("desc",des);
                startActivity(intent);
            }
        });
        authorities = getApplicationContext().getPackageName() + ".fileprovider";
        constraintLayout = findViewById(R.id.constraint);
      progressBar= findViewById(R.id.progressBar);
      progressBar.setVisibility(View.INVISIBLE );


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadFeeds.this, UploadActivity.class);
                Toast.makeText(UploadFeeds.this, "Please Select An Image", Toast.LENGTH_LONG).show();
                dispatchGalleryIntent();
            }
        });



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabaseWithDownloadURL(global_url,"Field1");
            }
        });

    }

    private void dispatchGalleryIntent() {
        Intent galleyIntent = new Intent();
        galleyIntent.setAction(galleyIntent.ACTION_GET_CONTENT);
        galleyIntent.setType("image/*");
        if (galleyIntent.resolveActivity(getPackageManager()) != null) {
            mImageFile = new File(UploadFeeds.this.getFilesDir(), new Date().getTime() + ".jpg");
            Uri imageFileUri = FileProvider.getUriForFile(this, authorities, mImageFile);
            galleyIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

            startActivityForResult(Intent.createChooser(galleyIntent,"Select picture"), GALLERY_REQUEST);
        }
    }

    void uploadToDatabase() {
        String description = desc.getText().toString();
        String link = link_editText.getText().toString();
        map.put("description", description);
        map.put("link", link);
        progressBar.setVisibility(View.VISIBLE);
        db.collection("feeds").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar.make(constraintLayout, "Upload Successful!", Snackbar.LENGTH_LONG);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar.make(constraintLayout, "An Error Occurred", Snackbar.LENGTH_LONG);


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICTURE_REQUEST_INTEGER && resultCode == RESULT_OK) {
//            // data will be null cause we are writing to a file
//            // https://stackoverflow.com/questions/3879992/how-to-get-bitmap-from-an-uri
//            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(mImageFile));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (!mImageFile.delete()) {
//                Log.d(TAG, "onActivityResult: DELETE FAILED");
//                Toast.makeText(this, "File error", Toast.LENGTH_SHORT).show();
//            }

//            FirebaseUser user = mAuth.getCurrentUser();
//            Date date = new Date();
//
//            imageBitmap = bitmap;
//            imageFilename = user.getEmail() + "/" + date.getTime() + ".jpg";
//            uploadImage();

            if (requestCode == GALLERY_REQUEST) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                imageFilename= user.getEmail() + "/"  + ".jpg";

                final StorageReference singleImageRef = imagesStorageRef.child(imageFilename);
            singleImageRef.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    singleImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            //Do what you want with the url
                           global_url= downloadUrl.toString();

                            Log.d(TAG, "onSuccess: " + global_url);

                        }
                    });


                }
            });
        }
    }

    public void uploadImage() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
       imageFilename= user.getEmail() + "/"  + ".jpg";
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
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        updateDatabaseWithDownloadURL(downloadURL, user.toString());
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

    private void updateDatabaseWithDownloadURL(String downloadURL, String user) {
        map.put("link", downloadURL);
        long time = System.currentTimeMillis();
        map.put("tag", desc.getText().toString());
        map.put("time", time);
        map.put("Name", "FieldGuide1");
        map.put("likes", 0);
        db.collection("feeds").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Success");
                ConstraintLayout constraintLayout=findViewById(R.id.constraint);
                Snackbar bar=Snackbar.make(constraintLayout,"Upload Successfull",Snackbar.LENGTH_LONG);
                bar.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed");

            }
        });
    }
}


